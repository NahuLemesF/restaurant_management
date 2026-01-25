package com.nahulemes.restaurantmanagement.dto.order;

import com.nahulemes.restaurantmanagement.dto.client.ClientResponseDTO;
import com.nahulemes.restaurantmanagement.dto.dish.DishResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        ClientResponseDTO client,
        List<DishResponseDTO> dishes,
        BigDecimal totalPrice,
        LocalDateTime orderDate
) {
}
