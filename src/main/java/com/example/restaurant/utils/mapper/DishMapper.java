package com.example.restaurant.utils.mapper;

import com.example.restaurant.dto.dish.DishRequestDTO;
import com.example.restaurant.dto.dish.DishResponseDTO;
import com.example.restaurant.models.Dish;
import com.example.restaurant.models.Menu;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DishMapper {

    public static DishResponseDTO convertToDto(Dish dish) {
        return new DishResponseDTO(
                dish.getId(),
                dish.getName(),
                dish.getDescription(),
                dish.getPrice().setScale(2, RoundingMode.HALF_UP),
                dish.getDishType().getName(),
                dish.getMenu().getName()
        );
    }

    public static Dish convertToEntity(DishRequestDTO dto, Menu menu) {
        Dish dish = new Dish();
        dish.setName(dto.name());
        dish.setDescription(dto.description());
        dish.setPrice(dto.price().setScale(2, RoundingMode.HALF_UP));
        dish.setMenu(menu);
        return dish;
    }

}
