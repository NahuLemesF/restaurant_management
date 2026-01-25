package com.nahulemes.restaurantmanagement.services.dish;

import com.nahulemes.restaurantmanagement.constants.DishType;
import com.nahulemes.restaurantmanagement.constants.EventType;
import com.nahulemes.restaurantmanagement.models.Dish;
import com.nahulemes.restaurantmanagement.observers.DishSubject;
import com.nahulemes.restaurantmanagement.repositories.IDishRepository;
import com.nahulemes.restaurantmanagement.repositories.IOrderRepository;
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
