package com.nahulemes.restaurantmanagement.handlers;

import com.nahulemes.restaurantmanagement.handlers.interfaces.IOrderHandler;
import com.nahulemes.restaurantmanagement.models.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


class OrderProcessingChainTest {

    private OrderProcessingChain orderProcessingChain;
    private IOrderHandler handler1;
    private IOrderHandler handler2;
    private Order order;

    @BeforeEach
    void setUp() {
        handler1 = mock(IOrderHandler.class);
        handler2 = mock(IOrderHandler.class);

        orderProcessingChain = new OrderProcessingChain(List.of(handler1, handler2));

        order = new Order();
        order.setId(1L);
    }

    @Test
    @DisplayName("Procesar orden con múltiples handlers")
    void processOrder() {
        orderProcessingChain.process(order);

        verify(handler1, times(1)).handle(order);
        verify(handler2, times(1)).handle(order);
    }
}
