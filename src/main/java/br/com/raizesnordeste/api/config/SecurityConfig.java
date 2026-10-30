package br.com.raizesnordeste.api.config;

import br.com.raizesnordeste.api.infra.security.SecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)

                // Configura a API para não guardar sessão (exigindo o envio do Token em cada requisição)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                // Mapeamento das regras de acesso
                .authorizeHttpRequests(auth -> auth
                        // Libera Swagger e documentação OpenAPI
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Libera consola do H2
                        .requestMatchers("/h2-console/**").permitAll()
                        // Libera o login para todos
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        // Libera a criação de novos usuários (para que clientes possam se cadastrar sozinhos)
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()

                        // Exemplo de regra futura: apenas ADMIN pode cadastrar produtos
                        // .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")

                        // Trava absolutamente todas as outras rotas (exigirá Token válido)
                        .anyRequest().authenticated()
                )

                // Injeta a nossa "catraca" ANTES da validação padrão do Spring
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    // Exporta o AuthenticationManager para o nosso AuthenticationController poder utilizá-lo
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Configura o algoritmo de criptografia padrão do Spring (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
