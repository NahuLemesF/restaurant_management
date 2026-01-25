package com.example.restaurant.services.dish;

import com.example.restaurant.constants.DishType;
import com.example.restaurant.constants.EventType;
import com.example.restaurant.models.Dish;
import com.example.restaurant.observers.DishSubject;
import com.example.restaurant.repositories.IDishRepository;
import com.example.restaurant.repositories.IOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IsPopularDishService {

    private final IDishRepository dishRepository;
    private final IOrderRepository orderRepository;
    private final DishSubject dishSubject;

    public IsPopularDishService(IDishRepository dishRepository, IOrderRepository orderRepository, DishSubject dishSubject) {
        this.dishRepository = dishRepository;
        this.orderRepository = orderRepository;
        this.dishSubject = dishSubject;
    }

    @Transactional
    public void markPopularDishes(List<Dish> dishes) {
        dishes.forEach(dish -> {
            Long orderCount = orderRepository.countByDishesId(dish.getId());
            markAsPopularIfNeeded(dish, orderCount);
        });
    }

    private void markAsPopularIfNeeded(Dish dish, Long orderCount) {
        if (orderCount >= 100) {
            dish.setDishType(DishType.POPULAR);
            dishRepository.save(dish);
            dishSubject.notifyObservers(EventType.UPDATE, dish);
        }
    }
}
