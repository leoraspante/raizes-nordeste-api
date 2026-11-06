package br.com.raizesnordeste.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração da documentação interativa da API utilizando o padrão OpenAPI 3.
 * Expõe as rotas, contratos de DTOs e configura a injeção global do cabeçalho
 * de autorização (Bearer Token) para testes dinâmicos das rotas protegidas pelo Spring Security.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Raizes do Nordeste API")
                        .version("v1.0.0")
                        .description("API REST desenvolvida para a Atividade Eletiva IV - Uninter.")
                        .contact(new Contact()
                                .name("Leonardo Raspante - RU: 4324217")
                                .email("leonardo.raspante@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
