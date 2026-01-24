package com.example.restaurant.services.client;

import com.example.restaurant.constants.ClientType;
import com.example.restaurant.constants.EventType;
import com.example.restaurant.models.Client;
import com.example.restaurant.observers.ClientSubject;
import com.example.restaurant.repositories.IClientRepository;
import com.example.restaurant.repositories.IOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;


class IsFrequentClientServiceTest {

    private IOrderRepository orderRepository;
    private IClientRepository clientRepository;
    private ClientSubject clientSubject;
    private FrequentClientService frequentClientService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(IOrderRepository.class);
        clientRepository = mock(IClientRepository.class);
        clientSubject = mock(ClientSubject.class);
        frequentClientService = new FrequentClientService(orderRepository, clientRepository, clientSubject);
    }

    @Test
    @DisplayName("Test FrequentClientService updateClientTypeIfFrequent - Client Becomes Frequent")
    void testExecuteClientBecomesFrequent() {
        Client client = new Client(1L, "John", "Doe", "john.doe@example.com", ClientType.COMMON);

        when(orderRepository.countByClientId(client.getId())).thenReturn(10L);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);

        frequentClientService.updateClientTypeIfFrequent(client.getId());

        assertEquals(ClientType.FREQUENT, client.getClientType());
        verify(clientRepository).save(client);

        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        verify(clientSubject).notifyObservers(eq(EventType.UPDATE), clientCaptor.capture());
        assertEquals(client, clientCaptor.getValue());
    }

    @Test
    @DisplayName("Test FrequentClientService updateClientTypeIfFrequent - Client Does Not Become Frequent")
    void testExecuteClientDoesNotBecomeFrequent() {
        Client client = new Client(1L, "John", "Doe", "john.doe@example.com", ClientType.COMMON);

        when(orderRepository.countByClientId(client.getId())).thenReturn(5L);

        frequentClientService.updateClientTypeIfFrequent(client.getId());

        assertEquals(ClientType.COMMON, client.getClientType());
        verify(clientRepository, never()).save(client);
        verify(clientSubject, never()).notifyObservers(any(), any());
    }
}

