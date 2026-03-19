package com.nahulemes.restaurantmanagement.observers;

import com.nahulemes.restaurantmanagement.models.Dish;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DishSubject extends GenericSubject<Dish> {

    private final DishNotificationObserver dishNotificationObserver;

    public DishSubject(DishNotificationObserver dishNotificationObserver) {
        this.dishNotificationObserver = dishNotificationObserver;
    }

    @PostConstruct
    public void init() {
        addObserver(dishNotificationObserver);
    }
}
