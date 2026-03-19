package com.nahulemes.restaurantmanagement.observers;

import com.nahulemes.restaurantmanagement.models.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderSubject extends GenericSubject<Order> {

    public OrderSubject() {
        super();
    }
}
