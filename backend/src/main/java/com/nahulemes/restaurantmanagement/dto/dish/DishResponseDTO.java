package com.nahulemes.restaurantmanagement.dto.dish;

import java.math.BigDecimal;

public record DishResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String dishType,
        String menuName,
        String imageUrl
) {
}
