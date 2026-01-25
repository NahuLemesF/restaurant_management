package com.nahulemes.restaurantmanagement.handlers;

import com.nahulemes.restaurantmanagement.handlers.interfaces.IOrderHandler;
import com.nahulemes.restaurantmanagement.models.Order;
import com.nahulemes.restaurantmanagement.services.client.FrequentClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FrequentClientHandler implements IOrderHandler {

    private final FrequentClientService frequentClientService;

    @Autowired
    public FrequentClientHandler(FrequentClientService frequentClientService) {
        this.frequentClientService = frequentClientService;
    }

    @Override
    public void handle(Order order) {
        frequentClientService.updateClientTypeIfFrequent(order.getClient().getId());
    }
}
