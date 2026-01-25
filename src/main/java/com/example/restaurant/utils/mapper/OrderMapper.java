package com.example.restaurant.utils.mapper;

import com.example.restaurant.dto.order.OrderResponseDTO;
import com.example.restaurant.models.Order;
import com.example.restaurant.models.Client;
import com.example.restaurant.models.Dish;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class OrderMapper {

    public static OrderResponseDTO toDto(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                ClientMapper.toDto(order.getClient()),
                order.getDishes().stream()
                        .map(DishMapper::convertToDto)
                        .toList(),
                getTotalPrice(order),
                order.getOrderDate()
        );
    }

    private static BigDecimal getTotalPrice(Order order) {
        return order.getTotalPrice() != null
                ? order.getTotalPrice().setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
    }

    public static Order toEntity(Client client, List<Dish> dishes) {
        Order order = new Order();
        order.setClient(client);
        order.setDishes(dishes);
        return order;
    }
}
