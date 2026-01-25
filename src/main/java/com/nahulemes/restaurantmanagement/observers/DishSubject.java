package com.nahulemes.restaurantmanagement.observers;

import com.nahulemes.restaurantmanagement.models.Dish;
import org.springframework.stereotype.Component;

@Component
public class DishSubject extends GenericSubject<Dish> {

    public DishSubject() {
        super();
    }
}
