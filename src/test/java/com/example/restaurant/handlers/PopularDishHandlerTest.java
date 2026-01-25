package com.example.restaurant.handlers;

import com.example.restaurant.models.Dish;
import com.example.restaurant.models.Order;
import com.example.restaurant.services.dish.IsPopularDishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class PopularDishHandlerTest {

    private PopularDishHandler popularDishHandler;
    private IsPopularDishService isPopularDishService;

    @BeforeEach
    void setUp() {
        isPopularDishService = mock(IsPopularDishService.class);
        popularDishHandler = new PopularDishHandler(isPopularDishService);
    }

    @Test
    void testHandle() {
        Dish dish1 = new Dish();
        Dish dish2 = new Dish();
        List<Dish> dishes = List.of(dish1, dish2);
        Order order = new Order();
        order.setDishes(dishes);

        popularDishHandler.handle(order);

        verify(isPopularDishService, times(1)).markPopularDishes(dishes);
    }
}
