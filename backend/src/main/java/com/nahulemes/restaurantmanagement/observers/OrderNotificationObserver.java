package com.nahulemes.restaurantmanagement.observers;

import com.nahulemes.restaurantmanagement.constants.EventType;
import com.nahulemes.restaurantmanagement.models.Order;
import com.nahulemes.restaurantmanagement.observers.interfaces.IObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderNotificationObserver implements IObserver<Order> {

    @Override
    public void update(EventType eventType, Order order) {
        switch (eventType) {
            case CREATE -> log.info("Notificación: Nueva orden creada para el cliente -> {}", order.getClient().getName());
            case UPDATE -> log.info("Notificación: Orden actualizada -> ID {}", order.getId());
            case DELETE -> log.info("Notificación: Orden eliminada -> ID {}", order.getId());
        }
    }
}
