package com.nahulemes.restaurantmanagement.handlers;

import com.nahulemes.restaurantmanagement.models.Client;
import com.nahulemes.restaurantmanagement.models.Order;
import com.nahulemes.restaurantmanagement.services.client.FrequentClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class FrequentClientHandlerTest {

    private FrequentClientHandler frequentClientHandler;
    private FrequentClientService frequentClientService;

    @BeforeEach
    void setUp() {
        frequentClientService = mock(FrequentClientService.class);
        frequentClientHandler = new FrequentClientHandler(frequentClientService);
    }

    @Test
    void testHandle() {
        Client client = new Client();
        client.setId(1L);
        Order order = new Order();
        order.setClient(client);

        frequentClientHandler.handle(order);

        verify(frequentClientService, times(1)).updateClientTypeIfFrequent(1L);
    }
}
