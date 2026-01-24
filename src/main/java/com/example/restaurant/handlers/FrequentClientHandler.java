package com.example.restaurant.handlers;

import com.example.restaurant.handlers.interfaces.IOrderHandler;
import com.example.restaurant.models.Order;
import com.example.restaurant.services.client.FrequentClientService;
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
