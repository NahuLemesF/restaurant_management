package com.nahulemes.restaurantmanagement.observers;

import com.nahulemes.restaurantmanagement.constants.EventType;
import com.nahulemes.restaurantmanagement.models.Dish;
import com.nahulemes.restaurantmanagement.observers.interfaces.IObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DishNotificationObserver implements IObserver<Dish> {

    @Override
    public void update(EventType eventType, Dish dish) {
        switch (eventType) {
            case CREATE -> log.info("Notificación: Nuevo plato añadido -> {}", dish.getName());
            case UPDATE -> log.info("Notificación: Plato actualizado -> {}", dish.getName());
            case DELETE -> log.info("Notificación: Plato eliminado -> {}", dish.getName());
        }
    }
}
