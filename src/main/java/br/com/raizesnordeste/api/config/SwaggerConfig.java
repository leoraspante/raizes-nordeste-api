package br.com.raizesnordeste.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                                .email("leonardo.raspante@gmail.com")));

    }
}
