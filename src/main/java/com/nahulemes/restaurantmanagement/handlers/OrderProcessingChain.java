package com.nahulemes.restaurantmanagement.handlers;

import com.nahulemes.restaurantmanagement.handlers.interfaces.IOrderHandler;
import com.nahulemes.restaurantmanagement.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderProcessingChain {

    private final List<IOrderHandler> handlers;

    @Autowired
    public OrderProcessingChain(List<IOrderHandler> handlers) {
        this.handlers = handlers;
    }

    public void process(Order order) {
        handlers.forEach(handler -> handler.handle(order));
    }
}
