package com.example.restaurant.services.dish;

import com.example.restaurant.dto.dish.DishRequestDTO;
import com.example.restaurant.models.Dish;

import java.util.List;

public interface DishService {
    Dish create(DishRequestDTO dto);
    Dish getById(Long id);
    List<Dish> getAll();
    void delete(Long id);
    Dish update(Long id, DishRequestDTO dto);
}
