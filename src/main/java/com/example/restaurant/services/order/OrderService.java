package com.example.restaurant.services.order;

import com.example.restaurant.dto.order.OrderRequestDTO;
import com.example.restaurant.models.Order;

import java.util.List;

public interface OrderService {
    Order create(OrderRequestDTO dto);
    Order getById(Long id);
    List<Order> getAll();
    void delete(Long id);
    Order update(Long id, OrderRequestDTO dto);
}
