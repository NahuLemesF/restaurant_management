package com.nahulemes.restaurantmanagement.controllers;

import com.nahulemes.restaurantmanagement.dto.menu.MenuRequestDTO;
import com.nahulemes.restaurantmanagement.dto.menu.MenuResponseDTO;
import com.nahulemes.restaurantmanagement.models.Menu;
import com.nahulemes.restaurantmanagement.services.menu.MenuService;
import com.nahulemes.restaurantmanagement.utils.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menus")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuResponseDTO> addMenu(@RequestBody @Valid MenuRequestDTO menuRequestDTO) {
        Menu createdMenu = menuService.create(menuRequestDTO);
        MenuResponseDTO responseDTO = MenuMapper.convertToDto(createdMenu);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponseDTO> getMenuById(@PathVariable Long menuId) {
        Menu menu = menuService.getById(menuId);
        MenuResponseDTO responseDTO = MenuMapper.convertToDto(menu);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<MenuResponseDTO>> getAllMenus() {
        List<Menu> menus = menuService.getAll();
        List<MenuResponseDTO> responseDTOs = menus.stream()
                .map(MenuMapper::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<MenuResponseDTO> updateMenu(@PathVariable Long menuId, @RequestBody @Valid MenuRequestDTO menuRequestDTO) {
        Menu updatedMenu = menuService.update(menuId, menuRequestDTO);
        MenuResponseDTO responseDTO = MenuMapper.convertToDto(updatedMenu);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        menuService.delete(menuId);
        return ResponseEntity.noContent().build();
    }

}
