package com.nahulemes.restaurantmanagement.controllers;

import com.nahulemes.restaurantmanagement.dto.dish.DishRequestDTO;
import com.nahulemes.restaurantmanagement.dto.dish.DishResponseDTO;
import com.nahulemes.restaurantmanagement.models.Dish;
import com.nahulemes.restaurantmanagement.services.dish.DishService;
import com.nahulemes.restaurantmanagement.utils.mapper.DishMapper;
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

    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping
    public DishResponseDTO addDish(@RequestBody @Valid DishRequestDTO dishRequestDTO) {
        Dish createdDish = dishService.create(dishRequestDTO);
        return DishMapper.convertToDto(createdDish);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishResponseDTO> getDishById(@PathVariable Long id) {
        Dish dish = dishService.getById(id);
        DishResponseDTO responseDTO = DishMapper.convertToDto(dish);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<DishResponseDTO>> getAllDishes() {
        List<Dish> dishes = dishService.getAll();
        List<DishResponseDTO> responseDTOs = dishes.stream()
                .map(DishMapper::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishResponseDTO> updateDish(@PathVariable Long id, @RequestBody @Valid DishRequestDTO dishRequestDTO) {
        Dish updatedDish = dishService.update(id, dishRequestDTO);
        DishResponseDTO responseDTO = DishMapper.convertToDto(updatedDish);
        return ResponseEntity.ok(responseDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
