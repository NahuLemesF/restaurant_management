package com.example.restaurant.utils.mapper;

import com.example.restaurant.dto.menu.MenuRequestDTO;
import com.example.restaurant.dto.menu.MenuResponseDTO;
import com.example.restaurant.models.Menu;
import com.example.restaurant.models.Dish;

import java.util.Collections;
import java.util.List;

public class MenuMapper {

    public static MenuResponseDTO convertToDto(Menu menu) {
        return new MenuResponseDTO(
                menu.getId(),
                menu.getName(),
                menu.getDescription(),
                menu.getDishes().stream()
                        .map(DishMapper::convertToDto)
                        .toList()
        );
    }

    public static Menu convertToEntity(MenuRequestDTO dto, List<Dish> dishes) {
        Menu menu = new Menu();
        menu.setName(dto.name());
        menu.setDescription(dto.description());
        menu.setDishes(dishes != null ? dishes : Collections.emptyList());
        return menu;
    }
}
