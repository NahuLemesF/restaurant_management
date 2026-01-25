package com.example.restaurant.dto.client;

public record ClientResponseDTO(
        Long id,
        String name,
        String lastName,
        String email,
        String clientType
) {
}
