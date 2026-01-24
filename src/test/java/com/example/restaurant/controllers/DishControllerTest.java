package com.example.restaurant.controllers;

import com.example.restaurant.constants.DishType;
import com.example.restaurant.dto.dish.DishRequestDTO;
import com.example.restaurant.dto.dish.DishResponseDTO;
import com.example.restaurant.models.Dish;
import com.example.restaurant.models.Menu;
import com.example.restaurant.services.dish.DishService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

class DishControllerTest {

    private MockMvc mockMvc;
    private DishService dishService;
    private ObjectMapper objectMapper;

    private Dish dish;
    private Menu menu;

    @BeforeEach
    void setUp() {
        dishService = mock(DishService.class);
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(new DishController(dishService)).build();

        menu = new Menu(1L, "Lunch Menu", "Delicious menu options");
        dish = new Dish(1L, "Pasta", "Delicious pasta", 12.99F, DishType.COMMON, menu);
    }

    @Test
    @DisplayName("Add Dish")
    void addDish() throws Exception {
        when(dishService.create(any(DishRequestDTO.class))).thenReturn(dish);

        DishRequestDTO dishRequestDTO = new DishRequestDTO("Pasta", "Delicious pasta", 12.99F, 1L);

        mockMvc.perform(post("/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(dish.getId()))
                .andExpect(jsonPath("$.name").value(dish.getName()))
                .andExpect(jsonPath("$.description").value(dish.getDescription()))
                .andExpect(jsonPath("$.price").value(dish.getPrice()))
                .andExpect(jsonPath("$.menuName").value(dish.getMenu().getName()));

        verify(dishService).create(any(DishRequestDTO.class));
    }

    @Test
    @DisplayName("Get Dish by ID")
    void getDishById() throws Exception {
        when(dishService.getById(anyLong())).thenReturn(dish);

        mockMvc.perform(get("/dishes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(dish.getId()))
                .andExpect(jsonPath("$.name").value(dish.getName()))
                .andExpect(jsonPath("$.description").value(dish.getDescription()))
                .andExpect(jsonPath("$.price").value(dish.getPrice()))
                .andExpect(jsonPath("$.menuName").value(dish.getMenu().getName()));

        verify(dishService).getById(anyLong());
    }

    @Test
    @DisplayName("Get All Dishes")
    void getAllDishes() throws Exception {
        List<Dish> dishes = List.of(
                dish,
                new Dish(2L, "Pizza", "Delicious pizza", 15.99F, DishType.COMMON, menu)
        );

        when(dishService.getAll()).thenReturn(dishes);

        mockMvc.perform(get("/dishes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Pasta"))
                .andExpect(jsonPath("$[1].name").value("Pizza"));

        verify(dishService).getAll();
    }

    @Test
    @DisplayName("Update Dish")
    void updateDish() throws Exception {
        when(dishService.update(anyLong(), any(DishRequestDTO.class))).thenReturn(dish);

        DishRequestDTO dishRequestDTO = new DishRequestDTO("Pasta", "Delicious pasta", 12.99F, 1L);

        mockMvc.perform(put("/dishes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(dish.getId()))
                .andExpect(jsonPath("$.name").value(dish.getName()))
                .andExpect(jsonPath("$.description").value(dish.getDescription()))
                .andExpect(jsonPath("$.price").value(dish.getPrice()))
                .andExpect(jsonPath("$.menuName").value(dish.getMenu().getName()));

        verify(dishService).update(anyLong(), any(DishRequestDTO.class));
    }

    @Test
    @DisplayName("Delete Dish")
    void deleteDish() throws Exception {
        doNothing().when(dishService).delete(anyLong());

        mockMvc.perform(delete("/dishes/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(dishService).delete(anyLong());
    }

    @Test
    @DisplayName("Test DishResponseDTO Getters and Setters")
    void testDishResponseDTO() {
        DishResponseDTO dishResponseDTO = new DishResponseDTO();
        dishResponseDTO.setId(1L);
        dishResponseDTO.setName("Pasta");
        dishResponseDTO.setDescription("Delicious pasta");
        dishResponseDTO.setPrice(12.99F);
        dishResponseDTO.setDishType("COMMON");
        dishResponseDTO.setMenuName("Lunch Menu");

        assertEquals(1L, dishResponseDTO.getId());
        assertEquals("Pasta", dishResponseDTO.getName());
        assertEquals("Delicious pasta", dishResponseDTO.getDescription());
        assertEquals(12.99F, dishResponseDTO.getPrice());
        assertEquals("COMMON", dishResponseDTO.getDishType());
        assertEquals("Lunch Menu", dishResponseDTO.getMenuName());
    }
}