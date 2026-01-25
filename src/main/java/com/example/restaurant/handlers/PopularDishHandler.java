package com.example.restaurant.handlers;

import com.example.restaurant.handlers.interfaces.IOrderHandler;
import com.example.restaurant.models.Order;
import com.example.restaurant.services.dish.IsPopularDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PopularDishHandler implements IOrderHandler {

    private final IsPopularDishService isPopularDishService;

    @Autowired
    public PopularDishHandler(IsPopularDishService isPopularDishService) {
        this.isPopularDishService = isPopularDishService;
    }

    @Override
    public void handle(Order order) {
        isPopularDishService.markPopularDishes(order.getDishes());
    }
}
