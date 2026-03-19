package com.nahulemes.restaurantmanagement.services.dish;

import com.nahulemes.restaurantmanagement.dto.dish.DishRequestDTO;
import com.nahulemes.restaurantmanagement.models.Dish;

import java.util.List;

public interface DishService {
    Dish create(DishRequestDTO dto);
    Dish getById(Long id);
    List<Dish> getAll();
    void delete(Long id);
    Dish update(Long id, DishRequestDTO dto);
}
