package com.example.restaurant.services.menu;

import com.example.restaurant.dto.menu.MenuRequestDTO;
import com.example.restaurant.models.Menu;

import java.util.List;

public interface MenuService {
    Menu create(MenuRequestDTO dto);
    Menu getById(Long id);
    List<Menu> getAll();
    void delete(Long id);
    Menu update(Long id, MenuRequestDTO dto);
}
