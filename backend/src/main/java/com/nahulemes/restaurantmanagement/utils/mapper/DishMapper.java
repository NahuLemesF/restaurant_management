package com.nahulemes.restaurantmanagement.utils.mapper;

import com.nahulemes.restaurantmanagement.dto.dish.DishRequestDTO;
import com.nahulemes.restaurantmanagement.dto.dish.DishResponseDTO;
import com.nahulemes.restaurantmanagement.models.Dish;
import com.nahulemes.restaurantmanagement.models.Menu;

import java.math.RoundingMode;

public class DishMapper {

    public static DishResponseDTO convertToDto(Dish dish) {
        return new DishResponseDTO(
                dish.getId(),
                dish.getName(),
                dish.getDescription(),
                dish.getPrice().setScale(2, RoundingMode.HALF_UP),
                dish.getDishType().getName(),
                dish.getMenu().getName(),
                dish.getImageUrl()
        );
    }

    public static Dish convertToEntity(DishRequestDTO dto, Menu menu) {
        Dish dish = new Dish();
        dish.setName(dto.name());
        dish.setDescription(dto.description());
        dish.setPrice(dto.price().setScale(2, RoundingMode.HALF_UP));
        dish.setMenu(menu);
        dish.setImageUrl(dto.imageUrl());
        return dish;
    }

}
