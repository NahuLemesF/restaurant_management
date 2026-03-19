package com.nahulemes.restaurantmanagement.services.dish;

import com.nahulemes.restaurantmanagement.constants.DishType;
import com.nahulemes.restaurantmanagement.constants.EventType;
import com.nahulemes.restaurantmanagement.models.Dish;
import com.nahulemes.restaurantmanagement.observers.DishSubject;
import com.nahulemes.restaurantmanagement.repositories.IDishRepository;
import com.nahulemes.restaurantmanagement.repositories.IOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;

class IsPopularDishServiceTest {

    private IDishRepository dishRepository;
    private IOrderRepository orderRepository;
    private DishSubject dishSubject;
    private IsPopularDishService isPopularDishService;

    @BeforeEach
    void setUp() {
        dishRepository = mock(IDishRepository.class);
        orderRepository = mock(IOrderRepository.class);
        dishSubject = mock(DishSubject.class);
        isPopularDishService = new IsPopularDishService(dishRepository, orderRepository, dishSubject);
    }

    @Test
    @DisplayName("Test IsPopularDishService markPopularDishes method - Mark as Popular")
    void testMarkPopularDishesMarkAsPopular() {
        Dish dish1 = new Dish();
        dish1.setId(1L);
        dish1.setName("Pasta");
        dish1.setDescription("Delicious pasta with tomato sauce");
        dish1.setPrice(new BigDecimal("12.99"));

        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Pizza");
        dish2.setDescription("Cheesy pizza with pepperoni");
        dish2.setPrice(new BigDecimal("15.99"));

        List<Dish> dishes = Arrays.asList(dish1, dish2);

        when(orderRepository.countByDishesId(1L)).thenReturn(150L);
        when(orderRepository.countByDishesId(2L)).thenReturn(50L);

        isPopularDishService.markPopularDishes(dishes);

        verify(orderRepository).countByDishesId(1L);
        verify(orderRepository).countByDishesId(2L);

        verify(dishRepository).save(dish1);
        verify(dishSubject).notifyObservers(eq(EventType.UPDATE), eq(dish1));

        assertEquals(DishType.POPULAR, dish1.getDishType());
        assertEquals(DishType.COMMON, dish2.getDishType());
    }

    @Test
    @DisplayName("Test IsPopularDishService markPopularDishes method - No Popular Dishes")
    void testMarkPopularDishesNoPopularDishes() {
        Dish dish1 = new Dish();
        dish1.setId(1L);
        dish1.setName("Pasta");
        dish1.setDescription("Delicious pasta with tomato sauce");
        dish1.setPrice(new BigDecimal("12.99"));

        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Pizza");
        dish2.setDescription("Cheesy pizza with pepperoni");
        dish2.setPrice(new BigDecimal("15.99"));

        List<Dish> dishes = Arrays.asList(dish1, dish2);

        when(orderRepository.countByDishesId(1L)).thenReturn(50L);
        when(orderRepository.countByDishesId(2L)).thenReturn(30L);

        isPopularDishService.markPopularDishes(dishes);

        verify(orderRepository).countByDishesId(1L);
        verify(orderRepository).countByDishesId(2L);

        verify(dishRepository, never()).save(any(Dish.class));
        verify(dishSubject, never()).notifyObservers(any(), any());

        assertEquals(DishType.COMMON, dish1.getDishType());
        assertEquals(DishType.COMMON, dish2.getDishType());
    }
}
