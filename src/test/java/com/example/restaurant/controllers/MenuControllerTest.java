package com.example.restaurant.controllers;

import com.example.restaurant.dto.menu.MenuRequestDTO;
import com.example.restaurant.dto.menu.MenuResponseDTO;
import com.example.restaurant.models.Menu;
import com.example.restaurant.services.dish.GetDishByIdService;
import com.example.restaurant.services.menu.AddMenuService;
import com.example.restaurant.services.menu.DeleteMenuService;
import com.example.restaurant.services.menu.GetAllMenusService;
import com.example.restaurant.services.menu.GetMenuByIdService;
import com.example.restaurant.services.menu.UpdateMenuService;
import com.example.restaurant.utils.mapper.MenuMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

class MenuControllerTest {

    private WebTestClient webTestClient;
    private AddMenuService addMenuService;
    private GetMenuByIdService getMenuByIdService;
    private GetAllMenusService getAllMenusService;
    private UpdateMenuService updateMenuService;
    private DeleteMenuService deleteMenuService;
    private GetDishByIdService getDishByIdService;

    private Menu menu;

    @BeforeEach
    void setUp() {
        addMenuService = mock(AddMenuService.class);
        getMenuByIdService = mock(GetMenuByIdService.class);
        getAllMenusService = mock(GetAllMenusService.class);
        updateMenuService = mock(UpdateMenuService.class);
        deleteMenuService = mock(DeleteMenuService.class);
        getDishByIdService = mock(GetDishByIdService.class);

        webTestClient = WebTestClient.bindToController(new MenuController(
                addMenuService,
                getMenuByIdService,
                getAllMenusService,
                updateMenuService,
                deleteMenuService,
                getDishByIdService
        )).build();

        menu = new Menu(1L, "Lunch Menu", "Delicious menu options", new ArrayList<>());
    }

    @Test
    @DisplayName("Add Menu")
    void addMenu() {
        doNothing().when(addMenuService).execute(any(Menu.class));

        MenuRequestDTO menuRequestDTO = new MenuRequestDTO();
        menuRequestDTO.setName("Lunch Menu");
        menuRequestDTO.setDescription("Delicious menu options");

        webTestClient.post()
                .uri("/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(menuRequestDTO)
                .exchange()
                .expectStatus().isOk();

        verify(addMenuService).execute(any(Menu.class));
    }


    @Test
    @DisplayName("Get Menu by ID")
    void getMenuById() {
        when(getMenuByIdService.execute(anyLong())).thenReturn(menu);

        webTestClient.get()
                .uri("/menus/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(MenuResponseDTO.class)
                .value(response -> {
                    assertEquals(menu.getId(), response.getId());
                    assertEquals(menu.getName(), response.getName());
                    assertEquals(menu.getDescription(), response.getDescription());
                });

        verify(getMenuByIdService).execute(anyLong());
    }

    @Test
    @DisplayName("Get All Menus")
    void getAllMenus() {
        List<Menu> menus = List.of(
                menu,
                new Menu(2L, "Dinner Menu", "Evening menu options", new ArrayList<>())
        );

        when(getAllMenusService.execute()).thenReturn(menus);

        webTestClient.get()
                .uri("/menus")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(MenuResponseDTO.class)
                .hasSize(2)
                .value(response -> {
                    assertEquals("Lunch Menu", response.get(0).getName());
                    assertEquals("Dinner Menu", response.get(1).getName());
                });

        verify(getAllMenusService).execute();
    }

    @Test
    @DisplayName("Update Menu")
    void updateMenu() {
        when(updateMenuService.execute(anyLong(), any(Menu.class))).thenReturn(menu);

        MenuRequestDTO menuRequestDTO = new MenuRequestDTO();
        menuRequestDTO.setName("Lunch Menu");
        menuRequestDTO.setDescription("Delicious menu options");

        webTestClient.put()
                .uri("/menus/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(menuRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(MenuResponseDTO.class)
                .value(response -> {
                    assertEquals(menu.getId(), response.getId());
                    assertEquals(menu.getName(), response.getName());
                    assertEquals(menu.getDescription(), response.getDescription());
                });

        verify(updateMenuService).execute(anyLong(), any(Menu.class));
    }

    @Test
    @DisplayName("Delete Menu")
    void deleteMenu() {
        doNothing().when(deleteMenuService).execute(anyLong());

        webTestClient.delete()
                .uri("/menus/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        verify(deleteMenuService).execute(anyLong());
    }

    @Test
    void handlesNullDishes() {

        MenuRequestDTO requestDTO = new MenuRequestDTO();
        requestDTO.setName("Test Menu");
        requestDTO.setDescription("Test Description");


        Menu result = MenuMapper.convertToEntity(requestDTO, null);
        
        assertEquals("Test Menu", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertEquals(Collections.emptyList(), result.getDishes());
    }
}
