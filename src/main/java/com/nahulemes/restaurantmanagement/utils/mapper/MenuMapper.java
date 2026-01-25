package com.nahulemes.restaurantmanagement.utils.mapper;

import com.nahulemes.restaurantmanagement.dto.menu.MenuRequestDTO;
import com.nahulemes.restaurantmanagement.dto.menu.MenuResponseDTO;
import com.nahulemes.restaurantmanagement.models.Menu;
import com.nahulemes.restaurantmanagement.models.Dish;

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
