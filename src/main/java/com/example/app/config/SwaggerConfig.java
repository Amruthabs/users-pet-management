package com.example.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Users & Pets API")
                        .description("API for managing users, addresses and pets (H2 in-memory)")
                        .version("1.0.0"))
                .servers(List.of(
                        new Server().url("http://localhost:8080") // Replace with your actual API base URL
                ));
    }
}
