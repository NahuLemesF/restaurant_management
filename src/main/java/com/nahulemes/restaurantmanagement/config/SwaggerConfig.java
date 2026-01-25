package com.nahulemes.restaurantmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Restaurant Management API")
                        .version("1.0")
                        .description("This API handles restaurant operations, including menu management, orders, and customer management.")
                        .contact(new Contact()
                                .name("Support Team")
                                .email("support@example.com")
                        )
                );
    }
}
