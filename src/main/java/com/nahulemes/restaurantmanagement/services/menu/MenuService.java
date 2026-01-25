package com.nahulemes.restaurantmanagement.services.menu;

import com.nahulemes.restaurantmanagement.dto.menu.MenuRequestDTO;
import com.nahulemes.restaurantmanagement.models.Menu;

import java.util.List;

public interface MenuService {
    Menu create(MenuRequestDTO dto);
    Menu getById(Long id);
    List<Menu> getAll();
    void delete(Long id);
    Menu update(Long id, MenuRequestDTO dto);
}
