package com.example.restaurant.dto.menu;

import com.example.restaurant.dto.dish.DishResponseDTO;

import java.util.List;

public record MenuResponseDTO(
        Long id,
        String name,
        String description,
        List<DishResponseDTO> dishes
) {
}
