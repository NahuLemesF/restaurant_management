package com.example.restaurant.controllers;

import com.example.restaurant.constants.ClientType;
import com.example.restaurant.dto.client.ClientRequestDTO;
import com.example.restaurant.dto.client.ClientResponseDTO;
import com.example.restaurant.models.Client;
import com.example.restaurant.services.client.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class ClientControllerTest {

    private WebTestClient webTestClient;
    private ClientService clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        clientService = mock(ClientService.class);

        webTestClient = WebTestClient.bindToController(new ClientController(
                clientService
        )).build();

        client = new Client(1L, "Martin", "Garmendia", "holasoymartin@example.com", ClientType.COMMON);
    }

    @Test
    @DisplayName("Agregar cliente")
    void addClient() {
        when(clientService.create(any(ClientRequestDTO.class))).thenReturn(client);

        ClientRequestDTO requestDTO = new ClientRequestDTO();
        requestDTO.setName("Martin");
        requestDTO.setLastName("Garmendia");
        requestDTO.setEmail("holasoymartin@example.com");

        webTestClient.post()
                .uri("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ClientResponseDTO.class)
                .value(response -> {
                    assertEquals(client.getName(), response.getName());
                    assertEquals(client.getLastName(), response.getLastName());
                    assertEquals(client.getEmail(), response.getEmail());
                });

        verify(clientService).create(any(ClientRequestDTO.class));
    }

    @Test
    @DisplayName("Obtener cliente por ID")
    void getClientById() {
        when(clientService.getById(anyLong())).thenReturn(client);

        webTestClient.get()
                .uri("/clients/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ClientResponseDTO.class)
                .value(response -> {
                    assertEquals(client.getName(), response.getName());
                    assertEquals(client.getLastName(), response.getLastName());
                    assertEquals(client.getEmail(), response.getEmail());
                });

        verify(clientService).getById(anyLong());
    }

    @Test
    @DisplayName("Listar todos los clientes")
    void getAllClients() {
        List<Client> clients = List.of(
                client,
                new Client(2L, "Nahuel", "Lemes", "nahulemes@example.com", ClientType.FREQUENT)
        );

        when(clientService.getAll()).thenReturn(clients);

        webTestClient.get()
                .uri("/clients")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ClientResponseDTO.class)
                .hasSize(2)
                .value(response -> {
                    assertEquals("Martin", response.get(0).getName());
                    assertEquals("Nahuel", response.get(1).getName());
                });

        verify(clientService).getAll();
    }

    @Test
    @DisplayName("Actualizar cliente")
    void updateClient() {
        when(clientService.update(anyLong(), any(ClientRequestDTO.class))).thenReturn(client);

        ClientRequestDTO requestDTO = new ClientRequestDTO();
        requestDTO.setName("Martin");
        requestDTO.setLastName("Garmendia");
        requestDTO.setEmail("holasoymartin@example.com");

        webTestClient.put()
                .uri("/clients/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ClientResponseDTO.class)
                .value(response -> {
                    assertEquals(client.getName(), response.getName());
                    assertEquals(client.getLastName(), response.getLastName());
                    assertEquals(client.getEmail(), response.getEmail());
                });

        verify(clientService).update(anyLong(), any(ClientRequestDTO.class));
    }

    @Test
    @DisplayName("Eliminar cliente")
    void deleteClient() {
        doNothing().when(clientService).delete(anyLong());

        webTestClient.delete()
                .uri("/clients/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        verify(clientService).delete(anyLong());
    }
}
