package com.nahulemes.restaurantmanagement.observers;

import com.nahulemes.restaurantmanagement.models.Order;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class OrderSubject extends GenericSubject<Order> {

    private final OrderNotificationObserver orderNotificationObserver;

    public OrderSubject(OrderNotificationObserver orderNotificationObserver) {
        this.orderNotificationObserver = orderNotificationObserver;
    }

    @PostConstruct
    public void init() {
        addObserver(orderNotificationObserver);
    }
}
