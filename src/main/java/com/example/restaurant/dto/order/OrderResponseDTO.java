package com.example.restaurant.dto.order;

import com.example.restaurant.dto.client.ClientResponseDTO;
import com.example.restaurant.dto.dish.DishResponseDTO;

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
