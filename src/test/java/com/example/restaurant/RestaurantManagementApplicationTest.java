package com.example.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class RestaurantManagementApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void main() {
        RestaurantManagementApplication.main(new String[] {});
    }
}