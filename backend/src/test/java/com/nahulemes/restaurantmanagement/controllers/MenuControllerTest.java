package com.nahulemes.restaurantmanagement.controllers;

import com.nahulemes.restaurantmanagement.dto.menu.MenuRequestDTO;
import com.nahulemes.restaurantmanagement.models.Menu;
import com.nahulemes.restaurantmanagement.services.interfaces.IMenuService;
import com.nahulemes.restaurantmanagement.utils.mapper.MenuMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

class MenuControllerTest {

    private MockMvc mockMvc;
    private IMenuService menuService;
    private ObjectMapper objectMapper;

    private Menu menu;

    @BeforeEach
    void setUp() {
        menuService = mock(IMenuService.class);
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(new MenuController(menuService)).build();

        menu = new Menu(1L, "Lunch Menu", "Delicious menu options", new ArrayList<>());
    }

    @Test
    @DisplayName("Add Menu")
    void addMenu() throws Exception {
        when(menuService.create(any(MenuRequestDTO.class))).thenReturn(menu);

        MenuRequestDTO menuRequestDTO = new MenuRequestDTO("Lunch Menu", "Delicious menu options", null);

        mockMvc.perform(post("/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuRequestDTO)))
                .andExpect(status().isOk());

        verify(menuService).create(any(MenuRequestDTO.class));
    }


    @Test
    @DisplayName("Get Menu by ID")
    void getMenuById() throws Exception {
        when(menuService.getById(anyLong())).thenReturn(menu);

        mockMvc.perform(get("/menus/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(menu.getId()))
                .andExpect(jsonPath("$.name").value(menu.getName()))
                .andExpect(jsonPath("$.description").value(menu.getDescription()));

        verify(menuService).getById(anyLong());
    }

    @Test
    @DisplayName("Get All Menus")
    void getAllMenus() throws Exception {
        List<Menu> menus = List.of(
                menu,
                new Menu(2L, "Dinner Menu", "Evening menu options", new ArrayList<>())
        );

        when(menuService.getAll()).thenReturn(menus);

        mockMvc.perform(get("/menus"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Lunch Menu"))
                .andExpect(jsonPath("$[1].name").value("Dinner Menu"));

        verify(menuService).getAll();
    }

    @Test
    @DisplayName("Update Menu")
    void updateMenu() throws Exception {
        when(menuService.update(anyLong(), any(MenuRequestDTO.class))).thenReturn(menu);

        MenuRequestDTO menuRequestDTO = new MenuRequestDTO("Lunch Menu", "Delicious menu options", null);

        mockMvc.perform(put("/menus/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(menu.getId()))
                .andExpect(jsonPath("$.name").value(menu.getName()))
                .andExpect(jsonPath("$.description").value(menu.getDescription()));

        verify(menuService).update(anyLong(), any(MenuRequestDTO.class));
    }

    @Test
    @DisplayName("Delete Menu")
    void deleteMenu() throws Exception {
        doNothing().when(menuService).delete(anyLong());

        mockMvc.perform(delete("/menus/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(menuService).delete(anyLong());
    }

    @Test
    void handlesNullDishes() {
        MenuRequestDTO requestDTO = new MenuRequestDTO("Test Menu", "Test Description", null);

        Menu result = MenuMapper.convertToEntity(requestDTO, null);

        assertEquals("Test Menu", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertEquals(Collections.emptyList(), result.getDishes());
    }
}
