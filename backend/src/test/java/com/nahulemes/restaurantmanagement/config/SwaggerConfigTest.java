package com.nahulemes.restaurantmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SwaggerConfigTest {

    @Test
    void testCustomOpenAPIConfiguration() {
        SwaggerConfig swaggerConfig = new SwaggerConfig();
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("Restaurant Management API", openAPI.getInfo().getTitle());
        assertEquals("1.0", openAPI.getInfo().getVersion());
        assertEquals("This API handles restaurant operations, including menu management, orders, and customer management.",
                     openAPI.getInfo().getDescription());
        assertNotNull(openAPI.getInfo().getContact());
        assertEquals("Support Team", openAPI.getInfo().getContact().getName());
        assertEquals("support@example.com", openAPI.getInfo().getContact().getEmail());
    }
}

