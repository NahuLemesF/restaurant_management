package com.nahulemes.restaurantmanagement.services.interfaces;

import com.nahulemes.restaurantmanagement.dto.menu.MenuRequestDTO;
import com.nahulemes.restaurantmanagement.models.Menu;

import java.util.List;

/**
 * Puerto de entrada (inbound port) para las operaciones de Menús.
 * Los controladores dependen ÚNICAMENTE de esta interfaz (DIP).
 */
public interface IMenuService {
    Menu create(MenuRequestDTO dto);
    Menu getById(Long id);
    List<Menu> getAll();
    void delete(Long id);
    Menu update(Long id, MenuRequestDTO dto);
}
