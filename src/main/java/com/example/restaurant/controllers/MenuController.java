package com.example.restaurant.controllers;

import com.example.restaurant.dto.menu.MenuRequestDTO;
import com.example.restaurant.dto.menu.MenuResponseDTO;
import com.example.restaurant.models.Menu;
import com.example.restaurant.models.Dish;
import com.example.restaurant.services.dish.GetDishByIdService;
import com.example.restaurant.services.menu.AddMenuService;
import com.example.restaurant.services.menu.DeleteMenuService;
import com.example.restaurant.services.menu.GetAllMenusService;
import com.example.restaurant.services.menu.GetMenuByIdService;
import com.example.restaurant.services.menu.UpdateMenuService;
import com.example.restaurant.utils.mapper.MenuMapper;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menus")
public class MenuController {

    private final AddMenuService addMenuService;
    private final GetMenuByIdService getMenuByIdService;
    private final GetAllMenusService getAllMenusService;
    private final UpdateMenuService updateMenuService;
    private final DeleteMenuService deleteMenuService;
    private final GetDishByIdService getDishByIdService;

    @Autowired
    public MenuController(AddMenuService addMenuService, GetMenuByIdService getMenuByIdService, GetAllMenusService getAllMenusService, UpdateMenuService updateMenuService, DeleteMenuService deleteMenuService, GetDishByIdService getDishByIdService) {
        this.addMenuService = addMenuService;
        this.getMenuByIdService = getMenuByIdService;
        this.getAllMenusService = getAllMenusService;
        this.updateMenuService = updateMenuService;
        this.deleteMenuService = deleteMenuService;
        this.getDishByIdService = getDishByIdService;
    }

    @PostMapping
    public ResponseEntity<MenuResponseDTO> addMenu(@RequestBody @Valid MenuRequestDTO menuRequestDTO) {
        Menu menu = createOrUpdateMenu(menuRequestDTO);
        addMenuService.execute(menu);
        MenuResponseDTO responseDTO = MenuMapper.convertToDto(menu);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponseDTO> getMenuById(@PathVariable Long menuId) {
        Menu menu = getMenuByIdService.execute(menuId);
        MenuResponseDTO responseDTO = MenuMapper.convertToDto(menu);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<MenuResponseDTO>> getAllMenus() {
        List<Menu> menus = getAllMenusService.execute();
        List<MenuResponseDTO> responseDTOs = menus.stream()
                .map(MenuMapper::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<MenuResponseDTO> updateMenu(@PathVariable Long menuId, @RequestBody @Valid MenuRequestDTO menuRequestDTO) {
        Menu menu = createOrUpdateMenu(menuRequestDTO);
        Menu updatedMenu = updateMenuService.execute(menuId, menu);
        MenuResponseDTO responseDTO = MenuMapper.convertToDto(updatedMenu);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        deleteMenuService.execute(menuId);
        return ResponseEntity.noContent().build();
    }

    private Menu createOrUpdateMenu(MenuRequestDTO menuRequestDTO) {
        List<Dish> dishes = Optional.ofNullable(menuRequestDTO.getDishIds())
                .orElse(Collections.emptyList())
                .stream()
                .map(getDishByIdService::execute)
                .collect(Collectors.toList());
        return MenuMapper.convertToEntity(menuRequestDTO, dishes);
    }

}
