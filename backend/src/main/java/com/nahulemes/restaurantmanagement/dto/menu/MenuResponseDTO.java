package com.nahulemes.restaurantmanagement.dto.menu;

import com.nahulemes.restaurantmanagement.dto.dish.DishResponseDTO;

import java.util.List;

public record MenuResponseDTO(
        Long id,
        String name,
        String description,
        List<DishResponseDTO> dishes
) {
}
