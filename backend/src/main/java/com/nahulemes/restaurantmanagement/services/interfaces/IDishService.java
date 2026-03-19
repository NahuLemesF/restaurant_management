package com.nahulemes.restaurantmanagement.services.interfaces;

import com.nahulemes.restaurantmanagement.dto.dish.DishRequestDTO;
import com.nahulemes.restaurantmanagement.models.Dish;

import java.util.List;

/**
 * Puerto de entrada (inbound port) para las operaciones de Platos.
 * Los controladores dependen ÚNICAMENTE de esta interfaz (DIP).
 */
public interface IDishService {
    Dish create(DishRequestDTO dto);
    Dish getById(Long id);
    List<Dish> getAll();
    void delete(Long id);
    Dish update(Long id, DishRequestDTO dto);
}
