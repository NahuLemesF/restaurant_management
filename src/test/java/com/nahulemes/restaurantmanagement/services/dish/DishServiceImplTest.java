package com.nahulemes.restaurantmanagement.services.dish;

import com.nahulemes.restaurantmanagement.constants.EventType;
import com.nahulemes.restaurantmanagement.dto.dish.DishRequestDTO;
import com.nahulemes.restaurantmanagement.exception.ResourceNotFoundException;
import com.nahulemes.restaurantmanagement.models.Dish;
import com.nahulemes.restaurantmanagement.models.Menu;
import com.nahulemes.restaurantmanagement.observers.DishSubject;
import com.nahulemes.restaurantmanagement.repositories.IDishRepository;
import com.nahulemes.restaurantmanagement.repositories.IMenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DishServiceImplTest {

    private IDishRepository dishRepository;
    private IMenuRepository menuRepository;
    private DishSubject dishSubject;
    private DishServiceImpl dishService;

    private Menu menu;
    private Dish dish;
    private DishRequestDTO dishRequestDTO;

    @BeforeEach
    void setUp() {
        dishRepository = mock(IDishRepository.class);
        menuRepository = mock(IMenuRepository.class);
        dishSubject = mock(DishSubject.class);
        dishService = new DishServiceImpl(dishRepository, menuRepository, dishSubject);

        menu = new Menu();
        menu.setId(1L);
        menu.setName("Test Menu");
        menu.setDescription("Test Description");

        dish = new Dish();
        dish.setId(1L);
        dish.setName("Test Dish");
        dish.setDescription("Test Description");
        dish.setPrice(new BigDecimal("10.99"));
        dish.setMenu(menu);

        dishRequestDTO = new DishRequestDTO("Test Dish", "Test Description", new BigDecimal("10.99"), 1L);
    }

    @Test
    @DisplayName("Create dish successfully")
    void testCreate() {
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(dishRepository.save(any(Dish.class))).thenReturn(dish);

        Dish result = dishService.create(dishRequestDTO);

        assertNotNull(result);
        assertEquals(dish.getId(), result.getId());
        assertEquals(dish.getName(), result.getName());
        verify(menuRepository).findById(1L);
        verify(dishRepository).save(any(Dish.class));
        verify(dishSubject).notifyObservers(EventType.CREATE, dish);
    }

    @Test
    @DisplayName("Create dish - Menu not found")
    void testCreateMenuNotFound() {
        when(menuRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            dishService.create(dishRequestDTO);
        });

        assertEquals("Menú no encontrado con id: '1'", exception.getMessage());
        verify(menuRepository).findById(1L);
        verify(dishRepository, never()).save(any(Dish.class));
        verify(dishSubject, never()).notifyObservers(any(), any());
    }

    @Test
    @DisplayName("Get dish by id successfully")
    void testGetById() {
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));

        Dish result = dishService.getById(1L);

        assertNotNull(result);
        assertEquals(dish.getId(), result.getId());
        assertEquals(dish.getName(), result.getName());
        verify(dishRepository).findById(1L);
    }

    @Test
    @DisplayName("Get dish by id - Not found")
    void testGetByIdNotFound() {
        when(dishRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            dishService.getById(1L);
        });

        assertEquals("Plato no encontrado con id: '1'", exception.getMessage());
        verify(dishRepository).findById(1L);
    }

    @Test
    @DisplayName("Get all dishes successfully")
    void testGetAll() {
        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Test Dish 2");
        List<Dish> dishes = Arrays.asList(dish, dish2);

        when(dishRepository.findAll()).thenReturn(dishes);

        List<Dish> result = dishService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dishes, result);
        verify(dishRepository).findAll();
    }

    @Test
    @DisplayName("Delete dish successfully")
    void testDelete() {
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        doNothing().when(dishRepository).delete(dish);

        dishService.delete(1L);

        verify(dishRepository).findById(1L);
        verify(dishRepository).delete(dish);
        verify(dishSubject).notifyObservers(EventType.DELETE, dish);
    }

    @Test
    @DisplayName("Delete dish - Not found")
    void testDeleteNotFound() {
        when(dishRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            dishService.delete(1L);
        });

        assertEquals("Plato no encontrado con id: '1'", exception.getMessage());
        verify(dishRepository).findById(1L);
        verify(dishRepository, never()).delete(any());
        verify(dishSubject, never()).notifyObservers(any(), any());
    }

    @Test
    @DisplayName("Update dish successfully")
    void testUpdate() {
        Menu newMenu = new Menu();
        newMenu.setId(2L);
        newMenu.setName("New Menu");

        DishRequestDTO updateDTO = new DishRequestDTO("Updated Dish", "Updated Description", new BigDecimal("15.99"), 2L);

        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(menuRepository.findById(2L)).thenReturn(Optional.of(newMenu));
        when(dishRepository.save(any(Dish.class))).thenReturn(dish);

        Dish result = dishService.update(1L, updateDTO);

        assertNotNull(result);
        assertEquals("Updated Dish", dish.getName());
        assertEquals("Updated Description", dish.getDescription());
        assertEquals(new BigDecimal("15.99"), dish.getPrice());
        assertEquals(newMenu, dish.getMenu());
        verify(dishRepository).findById(1L);
        verify(menuRepository).findById(2L);
        verify(dishRepository).save(dish);
        verify(dishSubject).notifyObservers(EventType.UPDATE, dish);
    }

    @Test
    @DisplayName("Update dish - Dish not found")
    void testUpdateDishNotFound() {
        DishRequestDTO updateDTO = new DishRequestDTO("Updated Dish", "Updated Description", new BigDecimal("15.99"), 1L);
        when(dishRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            dishService.update(1L, updateDTO);
        });

        assertEquals("Plato no encontrado con id: '1'", exception.getMessage());
        verify(dishRepository).findById(1L);
        verify(menuRepository, never()).findById(anyLong());
        verify(dishRepository, never()).save(any());
        verify(dishSubject, never()).notifyObservers(any(), any());
    }

    @Test
    @DisplayName("Update dish - Menu not found")
    void testUpdateMenuNotFound() {
        DishRequestDTO updateDTO = new DishRequestDTO("Updated Dish", "Updated Description", new BigDecimal("15.99"), 2L);
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(menuRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            dishService.update(1L, updateDTO);
        });

        assertEquals("Menú no encontrado con id: '2'", exception.getMessage());
        verify(dishRepository).findById(1L);
        verify(menuRepository).findById(2L);
        verify(dishRepository, never()).save(any());
        verify(dishSubject, never()).notifyObservers(any(), any());
    }
}
