package com.atelier.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.url}")
    private String url;

    @Bean
    public OpenAPI myOpenAPI() {
        Server server = new Server();
        server.setUrl(url);
        server.setDescription("Server URL based on active profile");

        Contact contact = new Contact();
        contact.setEmail("atelier@gmail.com");
        contact.setName("Atelier");
        contact.setUrl("https://www.atelier.com");

        Info info = new Info()
                .title("Atelier API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage tutorials.");

        return new OpenAPI().info(info).servers(List.of(server));
    }
}
