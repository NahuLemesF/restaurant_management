package com.example.restaurant.services.order;

import com.example.restaurant.constants.EventType;
import com.example.restaurant.models.Order;
import com.example.restaurant.observers.OrderSubject;
import com.example.restaurant.repositories.IOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.any;

class DeleteOrderServiceTest {

    private IOrderRepository orderRepository;
    private OrderSubject orderSubject;
    private DeleteOrderService deleteOrderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(IOrderRepository.class);
        orderSubject = mock(OrderSubject.class);
        deleteOrderService = new DeleteOrderService(orderRepository, orderSubject);
    }

    @Test
    @DisplayName("Test DeleteOrderService execute method - Success")
    void testExecuteSuccess() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        deleteOrderService.execute(1L);

        verify(orderRepository).findById(1L);
        verify(orderRepository).deleteById(1L);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderSubject).notifyObservers(eq(EventType.DELETE), orderCaptor.capture());
        assertEquals(order, orderCaptor.getValue());
    }

    @Test
    @DisplayName("Test DeleteOrderService execute method - Order Not Found")
    void testExecuteOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> deleteOrderService.execute(1L));
        assertEquals("Orden con el id 1 no encontrada", exception.getMessage());

        verify(orderRepository).findById(1L);
        verify(orderRepository, never()).deleteById(1L);
        verify(orderSubject, never()).notifyObservers(any(), any());
    }
}