package com.nahulemes.restaurantmanagement.handlers;

import com.nahulemes.restaurantmanagement.handlers.interfaces.IOrderHandler;
import com.nahulemes.restaurantmanagement.models.Order;
import com.nahulemes.restaurantmanagement.services.dish.IsPopularDishService;
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
