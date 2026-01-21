package com.example.restaurant.utils.mapper;

import com.example.restaurant.dto.menu.MenuRequestDTO;
import com.example.restaurant.dto.menu.MenuResponseDTO;
import com.example.restaurant.models.Menu;
import com.example.restaurant.models.Dish;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MenuMapper {

    public static MenuResponseDTO convertToDto(Menu menu) {
        MenuResponseDTO dto = new MenuResponseDTO();
        dto.setId(menu.getId());
        dto.setName(menu.getName());
        dto.setDescription(menu.getDescription());
        dto.setDishes(menu.getDishes().stream()
                .map(DishMapper::convertToDto)
                .collect(Collectors.toList()));
        return dto;
    }

    public static Menu convertToEntity(MenuRequestDTO dto, List<Dish> dishes) {
        Menu menu = new Menu();
        menu.setName(dto.getName());
        menu.setDescription(dto.getDescription());
        menu.setDishes(dishes != null ? dishes : Collections.emptyList());
        return menu;
    }
}
