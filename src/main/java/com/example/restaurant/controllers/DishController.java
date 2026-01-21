package com.example.restaurant.controllers;

import com.example.restaurant.dto.dish.DishRequestDTO;
import com.example.restaurant.dto.dish.DishResponseDTO;
import com.example.restaurant.models.Dish;
import com.example.restaurant.models.Menu;
import com.example.restaurant.services.dish.AddDishService;
import com.example.restaurant.services.dish.DeleteDishService;
import com.example.restaurant.services.dish.GetAllDishesService;
import com.example.restaurant.services.dish.GetDishByIdService;
import com.example.restaurant.services.dish.UpdateDishService;
import com.example.restaurant.services.menu.GetMenuByIdService;
import com.example.restaurant.utils.mapper.DishMapper;
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
@RequestMapping("/dishes")
public class DishController {

    private final AddDishService addDishService;
    private final GetDishByIdService getDishByIdService;
    private final GetAllDishesService getAllDishesService;
    private final UpdateDishService updateDishService;
    private final DeleteDishService deleteDishService;
    private final GetMenuByIdService getMenuByIdService;


    @Autowired
    public DishController(AddDishService addDishService, GetDishByIdService getDishByIdService, GetAllDishesService getAllDishesService, UpdateDishService updateDishService, DeleteDishService deleteDishService, GetMenuByIdService getMenuByIdService) {
        this.addDishService = addDishService;
        this.getDishByIdService = getDishByIdService;
        this.getAllDishesService = getAllDishesService;
        this.updateDishService = updateDishService;
        this.deleteDishService = deleteDishService;
        this.getMenuByIdService = getMenuByIdService;
    }

    @PostMapping
    public DishResponseDTO addDish(@RequestBody @Valid DishRequestDTO dishRequestDTO) {
        Menu menu = getMenuByIdService.execute(dishRequestDTO.getMenuId());
        Dish dish = DishMapper.convertToEntity(dishRequestDTO, menu);

        return DishMapper.convertToDto(addDishService.execute(dish));

    }

    @GetMapping("/{id}")
    public ResponseEntity<DishResponseDTO> getDishById(@PathVariable Long id) {
        Dish dish = getDishByIdService.execute(id);
        DishResponseDTO responseDTO = DishMapper.convertToDto(dish);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<DishResponseDTO>> getAllDishes() {
        List<Dish> dishes = getAllDishesService.execute();
        List<DishResponseDTO> responseDTOs = dishes.stream()
                .map(DishMapper::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishResponseDTO> updateDish(@PathVariable Long id, @RequestBody @Valid DishRequestDTO dishRequestDTO) {
        Menu menu = getMenuByIdService.execute(dishRequestDTO.getMenuId());
        Dish dishEntity = DishMapper.convertToEntity(dishRequestDTO, menu);
        Dish updatedDish = updateDishService.execute(id, dishEntity);
        DishResponseDTO responseDTO = DishMapper.convertToDto(updatedDish);
        return ResponseEntity.ok(responseDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        deleteDishService.execute(id);
        return ResponseEntity.noContent().build();
    }
}
