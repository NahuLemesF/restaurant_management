package com.example.restaurant.controllers;

import com.example.restaurant.constants.DishType;
import com.example.restaurant.dto.dish.DishRequestDTO;
import com.example.restaurant.dto.dish.DishResponseDTO;
import com.example.restaurant.models.Dish;
import com.example.restaurant.models.Menu;
import com.example.restaurant.services.dish.DishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DishControllerTest {

    private WebTestClient webTestClient;
    private DishService dishService;

    private Dish dish;
    private Menu menu;

    @BeforeEach
    void setUp() {
        dishService = mock(DishService.class);

        webTestClient = WebTestClient.bindToController(new DishController(dishService)).build();

        menu = new Menu(1L, "Lunch Menu", "Delicious menu options");
        dish = new Dish(1L, "Pasta", "Delicious pasta", 12.99F, DishType.COMMON, menu);
    }

    @Test
    @DisplayName("Add Dish")
    void addDish() {
        when(dishService.create(any(DishRequestDTO.class))).thenReturn(dish);

        DishRequestDTO dishRequestDTO = new DishRequestDTO("Pasta", "Delicious pasta", 12.99F, 1L);

        webTestClient.post()
                .uri("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dishRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DishResponseDTO.class)
                .value(response -> {
                    assertEquals(dish.getId(), response.getId());
                    assertEquals(dish.getName(), response.getName());
                    assertEquals(dish.getDescription(), response.getDescription());
                    assertEquals(dish.getPrice(), response.getPrice());
                    assertEquals(dish.getMenu().getName(), response.getMenuName());
                });

        verify(dishService).create(any(DishRequestDTO.class));
    }

    @Test
    @DisplayName("Get Dish by ID")
    void getDishById() {
        when(dishService.getById(anyLong())).thenReturn(dish);

        webTestClient.get()
                .uri("/dishes/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DishResponseDTO.class)
                .value(response -> {
                    assertEquals(dish.getId(), response.getId());
                    assertEquals(dish.getName(), response.getName());
                    assertEquals(dish.getDescription(), response.getDescription());
                    assertEquals(dish.getPrice(), response.getPrice());
                    assertEquals(dish.getMenu().getName(), response.getMenuName());
                });

        verify(dishService).getById(anyLong());
    }

    @Test
    @DisplayName("Get All Dishes")
    void getAllDishes() {
        List<Dish> dishes = List.of(
                dish,
                new Dish(2L, "Pizza", "Delicious pizza", 15.99F, DishType.COMMON, menu)
        );

        when(dishService.getAll()).thenReturn(dishes);

        webTestClient.get()
                .uri("/dishes")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(DishResponseDTO.class)
                .hasSize(2)
                .value(response -> {
                    assertEquals("Pasta", response.get(0).getName());
                    assertEquals("Pizza", response.get(1).getName());
                });

        verify(dishService).getAll();
    }

    @Test
    @DisplayName("Update Dish")
    void updateDish() {
        when(dishService.update(anyLong(), any(DishRequestDTO.class))).thenReturn(dish);

        DishRequestDTO dishRequestDTO = new DishRequestDTO("Pasta", "Delicious pasta", 12.99F, 1L);

        webTestClient.put()
                .uri("/dishes/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dishRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DishResponseDTO.class)
                .value(response -> {
                    assertEquals(dish.getId(), response.getId());
                    assertEquals(dish.getName(), response.getName());
                    assertEquals(dish.getDescription(), response.getDescription());
                    assertEquals(dish.getPrice(), response.getPrice());
                    assertEquals(dish.getMenu().getName(), response.getMenuName());
                });

        verify(dishService).update(anyLong(), any(DishRequestDTO.class));
    }

    @Test
    @DisplayName("Delete Dish")
    void deleteDish() {
        doNothing().when(dishService).delete(anyLong());

        webTestClient.delete()
                .uri("/dishes/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

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