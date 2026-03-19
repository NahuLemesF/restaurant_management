package com.nahulemes.restaurantmanagement.services.interfaces;

import com.nahulemes.restaurantmanagement.dto.order.OrderRequestDTO;
import com.nahulemes.restaurantmanagement.models.Order;

import java.util.List;

/**
 * Puerto de entrada (inbound port) para las operaciones de Órdenes.
 * Los controladores dependen ÚNICAMENTE de esta interfaz (DIP).
 */
public interface IOrderService {
    Order create(OrderRequestDTO dto);
    Order getById(Long id);
    List<Order> getAll();
    void delete(Long id);
    Order update(Long id, OrderRequestDTO dto);
}
