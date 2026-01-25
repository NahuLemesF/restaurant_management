package com.example.restaurant.services.menu;

import com.example.restaurant.constants.EventType;
import com.example.restaurant.dto.menu.MenuRequestDTO;
import com.example.restaurant.exception.ResourceNotFoundException;
import com.example.restaurant.models.Dish;
import com.example.restaurant.models.Menu;
import com.example.restaurant.observers.MenuSubject;
import com.example.restaurant.repositories.IDishRepository;
import com.example.restaurant.repositories.IMenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MenuServiceImplTest {

    private IMenuRepository menuRepository;
    private IDishRepository dishRepository;
    private MenuSubject menuSubject;
    private MenuServiceImpl menuService;

    private Menu menu;
    private Dish dish1;
    private Dish dish2;
    private MenuRequestDTO menuRequestDTO;

    @BeforeEach
    void setUp() {
        menuRepository = mock(IMenuRepository.class);
        dishRepository = mock(IDishRepository.class);
        menuSubject = mock(MenuSubject.class);
        menuService = new MenuServiceImpl(menuRepository, dishRepository, menuSubject);

        menu = new Menu();
        menu.setId(1L);
        menu.setName("Test Menu");
        menu.setDescription("Test Description");

        dish1 = new Dish();
        dish1.setId(1L);
        dish1.setName("Dish 1");

        dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Dish 2");

        menuRequestDTO = new MenuRequestDTO();
        menuRequestDTO.setName("Test Menu");
        menuRequestDTO.setDescription("Test Description");
        menuRequestDTO.setDishIds(Arrays.asList(1L, 2L));
    }

    @Test
    @DisplayName("Create menu successfully with dishes")
    void testCreateWithDishes() {
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish1));
        when(dishRepository.findById(2L)).thenReturn(Optional.of(dish2));
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        Menu result = menuService.create(menuRequestDTO);

        assertNotNull(result);
        assertEquals(menu.getId(), result.getId());
        assertEquals(menu.getName(), result.getName());
        verify(dishRepository).findById(1L);
        verify(dishRepository).findById(2L);
        verify(menuRepository).save(any(Menu.class));
        verify(menuSubject).notifyObservers(EventType.CREATE, menu);
    }

    @Test
    @DisplayName("Create menu successfully without dishes")
    void testCreateWithoutDishes() {
        MenuRequestDTO dtoWithoutDishes = new MenuRequestDTO();
        dtoWithoutDishes.setName("Test Menu");
        dtoWithoutDishes.setDescription("Test Description");
        dtoWithoutDishes.setDishIds(null);

        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        Menu result = menuService.create(dtoWithoutDishes);

        assertNotNull(result);
        verify(dishRepository, never()).findById(anyLong());
        verify(menuRepository).save(any(Menu.class));
        verify(menuSubject).notifyObservers(EventType.CREATE, menu);
    }

    @Test
    @DisplayName("Create menu - Dish not found")
    void testCreateDishNotFound() {
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish1));
        when(dishRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            menuService.create(menuRequestDTO);
        });

        assertEquals("Plato no encontrado con id: '2'", exception.getMessage());
        verify(dishRepository).findById(1L);
        verify(dishRepository).findById(2L);
        verify(menuRepository, never()).save(any());
        verify(menuSubject, never()).notifyObservers(any(), any());
    }

    @Test
    @DisplayName("Get menu by id successfully")
    void testGetById() {
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        Menu result = menuService.getById(1L);

        assertNotNull(result);
        assertEquals(menu.getId(), result.getId());
        assertEquals(menu.getName(), result.getName());
        verify(menuRepository).findById(1L);
    }

    @Test
    @DisplayName("Get menu by id - Not found")
    void testGetByIdNotFound() {
        when(menuRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            menuService.getById(1L);
        });

        assertEquals("Menú no encontrado con id: '1'", exception.getMessage());
        verify(menuRepository).findById(1L);
    }

    @Test
    @DisplayName("Get all menus successfully")
    void testGetAll() {
        Menu menu2 = new Menu();
        menu2.setId(2L);
        menu2.setName("Test Menu 2");
        List<Menu> menus = Arrays.asList(menu, menu2);

        when(menuRepository.findAll()).thenReturn(menus);

        List<Menu> result = menuService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(menus, result);
        verify(menuRepository).findAll();
    }

    @Test
    @DisplayName("Delete menu successfully")
    void testDelete() {
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        doNothing().when(menuRepository).delete(menu);

        menuService.delete(1L);

        verify(menuRepository).findById(1L);
        verify(menuRepository).delete(menu);
        verify(menuSubject).notifyObservers(EventType.DELETE, menu);
    }

    @Test
    @DisplayName("Delete menu - Not found")
    void testDeleteNotFound() {
        when(menuRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            menuService.delete(1L);
        });

        assertEquals("Menú no encontrado con id: '1'", exception.getMessage());
        verify(menuRepository).findById(1L);
        verify(menuRepository, never()).delete(any());
        verify(menuSubject, never()).notifyObservers(any(), any());
    }

    @Test
    @DisplayName("Update menu successfully with dishes")
    void testUpdateWithDishes() {
        MenuRequestDTO updateDTO = new MenuRequestDTO();
        updateDTO.setName("Updated Menu");
        updateDTO.setDescription("Updated Description");
        updateDTO.setDishIds(Arrays.asList(1L, 2L));

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish1));
        when(dishRepository.findById(2L)).thenReturn(Optional.of(dish2));
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        Menu result = menuService.update(1L, updateDTO);

        assertNotNull(result);
        assertEquals("Updated Menu", menu.getName());
        assertEquals("Updated Description", menu.getDescription());
        verify(menuRepository).findById(1L);
        verify(dishRepository).findById(1L);
        verify(dishRepository).findById(2L);
        verify(menuRepository).save(menu);
        verify(menuSubject).notifyObservers(EventType.UPDATE, menu);
    }

    @Test
    @DisplayName("Update menu successfully without dishes")
    void testUpdateWithoutDishes() {
        MenuRequestDTO updateDTO = new MenuRequestDTO();
        updateDTO.setName("Updated Menu");
        updateDTO.setDescription("Updated Description");
        updateDTO.setDishIds(null);

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        Menu result = menuService.update(1L, updateDTO);

        assertNotNull(result);
        assertEquals("Updated Menu", menu.getName());
        assertEquals("Updated Description", menu.getDescription());
        verify(menuRepository).findById(1L);
        verify(dishRepository, never()).findById(anyLong());
        verify(menuRepository).save(menu);
        verify(menuSubject).notifyObservers(EventType.UPDATE, menu);
    }

    @Test
    @DisplayName("Update menu - Menu not found")
    void testUpdateMenuNotFound() {
        MenuRequestDTO updateDTO = new MenuRequestDTO();
        updateDTO.setName("Updated Menu");
        updateDTO.setDescription("Updated Description");

        when(menuRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            menuService.update(1L, updateDTO);
        });

        assertEquals("Menú no encontrado con id: '1'", exception.getMessage());
        verify(menuRepository).findById(1L);
        verify(dishRepository, never()).findById(anyLong());
        verify(menuRepository, never()).save(any());
        verify(menuSubject, never()).notifyObservers(any(), any());
    }

    @Test
    @DisplayName("Update menu - Dish not found")
    void testUpdateDishNotFound() {
        MenuRequestDTO updateDTO = new MenuRequestDTO();
        updateDTO.setName("Updated Menu");
        updateDTO.setDescription("Updated Description");
        updateDTO.setDishIds(Arrays.asList(1L, 3L));

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish1));
        when(dishRepository.findById(3L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            menuService.update(1L, updateDTO);
        });

        assertEquals("Plato no encontrado con id: '3'", exception.getMessage());
        verify(menuRepository).findById(1L);
        verify(dishRepository).findById(1L);
        verify(dishRepository).findById(3L);
        verify(menuRepository, never()).save(any());
        verify(menuSubject, never()).notifyObservers(any(), any());
    }

    @Test
    @DisplayName("Create menu with empty dish list")
    void testCreateWithEmptyDishList() {
        MenuRequestDTO dtoWithEmptyList = new MenuRequestDTO();
        dtoWithEmptyList.setName("Test Menu");
        dtoWithEmptyList.setDescription("Test Description");
        dtoWithEmptyList.setDishIds(Collections.emptyList());

        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        Menu result = menuService.create(dtoWithEmptyList);

        assertNotNull(result);
        verify(dishRepository, never()).findById(anyLong());
        verify(menuRepository).save(any(Menu.class));
        verify(menuSubject).notifyObservers(EventType.CREATE, menu);
    }
}
