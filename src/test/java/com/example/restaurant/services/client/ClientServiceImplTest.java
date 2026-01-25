package com.example.restaurant.services.client;

import com.example.restaurant.constants.ClientType;
import com.example.restaurant.constants.EventType;
import com.example.restaurant.dto.client.ClientRequestDTO;
import com.example.restaurant.exception.ResourceNotFoundException;
import com.example.restaurant.models.Client;
import com.example.restaurant.observers.ClientSubject;
import com.example.restaurant.repositories.IClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    private IClientRepository clientRepository;
    private ClientSubject clientSubject;
    private ClientServiceImpl clientService;

    private Client client;
    private ClientRequestDTO clientRequestDTO;

    @BeforeEach
    void setUp() {
        clientRepository = mock(IClientRepository.class);
        clientSubject = mock(ClientSubject.class);
        clientService = new ClientServiceImpl(clientRepository, clientSubject);

        client = new Client();
        client.setId(1L);
        client.setName("John");
        client.setLastName("Doe");
        client.setEmail("john.doe@example.com");
        client.setClientType(ClientType.COMMON);

        clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setName("John");
        clientRequestDTO.setLastName("Doe");
        clientRequestDTO.setEmail("john.doe@example.com");
    }

    @Test
    @DisplayName("Create client successfully")
    void testCreate() {
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client result = clientService.create(clientRequestDTO);

        assertNotNull(result);
        assertEquals(client.getId(), result.getId());
        assertEquals(client.getName(), result.getName());
        assertEquals(client.getLastName(), result.getLastName());
        assertEquals(client.getEmail(), result.getEmail());
        verify(clientRepository).save(any(Client.class));
        verify(clientSubject).notifyObservers(EventType.CREATE, client);
    }

    @Test
    @DisplayName("Get client by id successfully")
    void testGetById() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Client result = clientService.getById(1L);

        assertNotNull(result);
        assertEquals(client.getId(), result.getId());
        assertEquals(client.getName(), result.getName());
        verify(clientRepository).findById(1L);
    }

    @Test
    @DisplayName("Get client by id - Not found")
    void testGetByIdNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientService.getById(1L);
        });

        assertEquals("Cliente no encontrado con id: '1'", exception.getMessage());
        verify(clientRepository).findById(1L);
    }

    @Test
    @DisplayName("Get all clients successfully")
    void testGetAll() {
        Client client2 = new Client();
        client2.setId(2L);
        client2.setName("Jane");
        client2.setLastName("Smith");
        List<Client> clients = Arrays.asList(client, client2);

        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> result = clientService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(clients, result);
        verify(clientRepository).findAll();
    }

    @Test
    @DisplayName("Delete client successfully")
    void testDelete() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        doNothing().when(clientRepository).delete(client);

        clientService.delete(1L);

        verify(clientRepository).findById(1L);
        verify(clientRepository).delete(client);
        verify(clientSubject).notifyObservers(EventType.DELETE, client);
    }

    @Test
    @DisplayName("Delete client - Not found")
    void testDeleteNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientService.delete(1L);
        });

        assertEquals("Cliente no encontrado con id: '1'", exception.getMessage());
        verify(clientRepository).findById(1L);
        verify(clientRepository, never()).delete(any());
        verify(clientSubject, never()).notifyObservers(any(), any());
    }

    @Test
    @DisplayName("Update client successfully")
    void testUpdate() {
        ClientRequestDTO updateDTO = new ClientRequestDTO();
        updateDTO.setName("Johnny");
        updateDTO.setLastName("Doe Updated");
        updateDTO.setEmail("johnny.updated@example.com");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client result = clientService.update(1L, updateDTO);

        assertNotNull(result);
        assertEquals("Johnny", client.getName());
        assertEquals("Doe Updated", client.getLastName());
        assertEquals("johnny.updated@example.com", client.getEmail());
        verify(clientRepository).findById(1L);
        verify(clientRepository).save(client);
        verify(clientSubject).notifyObservers(EventType.UPDATE, client);
    }

    @Test
    @DisplayName("Update client - Not found")
    void testUpdateNotFound() {
        ClientRequestDTO updateDTO = new ClientRequestDTO();
        updateDTO.setName("Johnny");
        updateDTO.setLastName("Doe Updated");
        updateDTO.setEmail("johnny.updated@example.com");

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientService.update(1L, updateDTO);
        });

        assertEquals("Cliente no encontrado con id: '1'", exception.getMessage());
        verify(clientRepository).findById(1L);
        verify(clientRepository, never()).save(any());
        verify(clientSubject, never()).notifyObservers(any(), any());
    }
}
