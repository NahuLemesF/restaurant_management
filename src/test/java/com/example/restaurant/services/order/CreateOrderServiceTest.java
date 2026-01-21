package com.example.restaurant.services.order;

import com.example.restaurant.constants.ClientType;
import com.example.restaurant.constants.EventType;
import com.example.restaurant.models.Client;
import com.example.restaurant.models.Dish;
import com.example.restaurant.models.Order;
import com.example.restaurant.observers.OrderSubject;
import com.example.restaurant.repositories.IOrderRepository;
import com.example.restaurant.services.dish.GetDishByIdService;
import com.example.restaurant.handlers.OrderProcessingChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

class CreateOrderServiceTest {

    private IOrderRepository orderRepository;
    private GetClientByIdService getClientByIdService;
    private GetDishByIdService getDishByIdService;
    private OrderSubject orderSubject;
    private OrderProcessingChain orderProcessingChain;
    private CreateOrderService createOrderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(IOrderRepository.class);
        getClientByIdService = mock(GetClientByIdService.class);
        getDishByIdService = mock(GetDishByIdService.class);
        orderSubject = mock(OrderSubject.class);
        orderProcessingChain = mock(OrderProcessingChain.class);
        createOrderService = new CreateOrderService(orderRepository, getClientByIdService, getDishByIdService, orderSubject, orderProcessingChain);
    }

    @Test
    @DisplayName("Test CreateOrderService execute method - Success")
    void testExecuteSuccess() {
        Client client = new Client();
        client.setId(1L);
        client.setClientType(ClientType.COMMON);

        Dish dish1 = new Dish();
        dish1.setId(1L);
        dish1.setPrice(10.0F);

        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setPrice(15.0F);

        List<Long> dishIds = Arrays.asList(1L, 2L);
        List<Dish> dishes = Arrays.asList(dish1, dish2);

        when(getClientByIdService.execute(1L)).thenReturn(client);
        when(getDishByIdService.execute(1L)).thenReturn(dish1);
        when(getDishByIdService.execute(2L)).thenReturn(dish2);

        Order order = new Order();
        order.setClient(client);
        order.setDishes(dishes);
        order.setTotalPrice(100.0F);

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = createOrderService.execute(1L, dishIds);

        assertEquals(order, result);
        verify(orderRepository).save(any(Order.class));
        verify(orderSubject).notifyObservers(eq(EventType.CREATE), any(Order.class));
    }
}