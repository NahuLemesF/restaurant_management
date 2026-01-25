package com.nahulemes.restaurantmanagement.services.order;

import com.nahulemes.restaurantmanagement.dto.order.OrderRequestDTO;
import com.nahulemes.restaurantmanagement.models.Order;

import java.util.List;

public interface OrderService {
    Order create(OrderRequestDTO dto);
    Order getById(Long id);
    List<Order> getAll();
    void delete(Long id);
    Order update(Long id, OrderRequestDTO dto);
}
