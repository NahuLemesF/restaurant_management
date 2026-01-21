package com.example.restaurant.utils.mapper;

import com.example.restaurant.dto.dish.DishRequestDTO;
import com.example.restaurant.dto.dish.DishResponseDTO;
import com.example.restaurant.models.Dish;
import com.example.restaurant.models.Menu;
import com.example.restaurant.utils.RoundToTwoDecimals;


public class DishMapper {



    public static DishResponseDTO convertToDto(Dish dish) {
        DishResponseDTO dto = new DishResponseDTO();
        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setDescription(dish.getDescription());
        dto.setPrice(RoundToTwoDecimals.roundToTwoDecimals(dish.getPrice()));
        dto.setDishType(dish.getDishType().getName());
        dto.setMenuName(dish.getMenu().getName());
        return dto;
    }

    public static Dish convertToEntity(DishRequestDTO dto, Menu menu) {
        Dish dish = new Dish();
        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setPrice(dto.getPrice());
        dish.setMenu(menu);
        return dish;
    }


}
