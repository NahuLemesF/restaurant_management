package com.example.restaurant.utils.converter;

import com.example.restaurant.dto.order.OrderResponseDTO;
import com.example.restaurant.models.Order;
import com.example.restaurant.models.Client;
import com.example.restaurant.models.Dish;
import com.example.restaurant.utils.RoundToTwoDecimals;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDtoConverter {

    public static OrderResponseDTO toDto(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setClient(ClientDtoConverter.convertToDto(order.getClient()));
        dto.setDishes(order.getDishes().stream()
                .map(DishDtoConverter::convertToDto)
                .collect(Collectors.toList()));
        dto.setTotalPrice(getTotalPrice(order));
        dto.setOrderDate(order.getOrderDate());
        return dto;
    }

    private static float getTotalPrice(Order order) {
        return order.getTotalPrice() != null ? RoundToTwoDecimals.roundToTwoDecimals(order.getTotalPrice()) : 0.0f;
    }

    public static Order toEntity(Client client, List<Dish> dishes) {
        Order order = new Order();
        order.setClient(client);
        order.setDishes(dishes);
        return order;
    }
}
