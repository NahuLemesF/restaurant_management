package com.example.restaurant.controllers;

import com.example.restaurant.constants.ClientType;
import com.example.restaurant.models.Client;
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
    private AddClientService addClientService;
    private GetClientByIdService getClientByIdService;
    private GetAllClientsService getAllClientsService;
    private UpdateClientService updateClientService;
    private DeleteClientService deleteClientService;

    private Client client;

    @BeforeEach
    void setUp() {
        addClientService = mock(AddClientService.class);
        getClientByIdService = mock(GetClientByIdService.class);
        getAllClientsService = mock(GetAllClientsService.class);
        updateClientService = mock(UpdateClientService.class);
        deleteClientService = mock(DeleteClientService.class);

        webTestClient = WebTestClient.bindToController(new ClientController(
                addClientService,
                getClientByIdService,
                getAllClientsService,
                updateClientService,
                deleteClientService
        )).build();

        client = new Client(1L, "Martin", "Garmendia", "holasoymartin@example.com", ClientType.COMMON);
    }

    @Test
    @DisplayName("Agregar cliente")
    void addClient() {
        when(addClientService.execute(any(Client.class))).thenReturn(client);

        webTestClient.post()
                .uri("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(client)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Client.class)
                .value(response -> {
                    assertEquals(client.getId(), response.getId());
                    assertEquals(client.getName(), response.getName());
                    assertEquals(client.getLastName(), response.getLastName());
                    assertEquals(client.getEmail(), response.getEmail());
                    assertEquals(client.getClientType(), response.getClientType());
                });

        verify(addClientService).execute(any(Client.class));
    }

    @Test
    @DisplayName("Obtener cliente por ID")
    void getClientById() {
        when(getClientByIdService.execute(anyLong())).thenReturn(client);

        webTestClient.get()
                .uri("/clients/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Client.class)
                .value(response -> {
                    assertEquals(client.getId(), response.getId());
                    assertEquals(client.getName(), response.getName());
                    assertEquals(client.getLastName(), response.getLastName());
                    assertEquals(client.getEmail(), response.getEmail());
                    assertEquals(client.getClientType(), response.getClientType());
                });

        verify(getClientByIdService).execute(anyLong());
    }

    @Test
    @DisplayName("Listar todos los clientes")
    void getAllClients() {
        List<Client> clients = List.of(
                client,
                new Client(2L, "Nahuel", "Lemes", "nahulemes@example.com", ClientType.FREQUENT)
        );

        when(getAllClientsService.execute()).thenReturn(clients);

        webTestClient.get()
                .uri("/clients")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Client.class)
                .hasSize(2)
                .value(response -> {
                    assertEquals("Martin", response.get(0).getName());
                    assertEquals("Nahuel", response.get(1).getName());
                });

        verify(getAllClientsService).execute();
    }

    @Test
    @DisplayName("Actualizar cliente")
    void updateClient() {
        when(updateClientService.execute(anyLong(), any(Client.class))).thenReturn(client);

        webTestClient.put()
                .uri("/clients/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(client)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Client.class)
                .value(response -> {
                    assertEquals(client.getId(), response.getId());
                    assertEquals(client.getName(), response.getName());
                    assertEquals(client.getLastName(), response.getLastName());
                    assertEquals(client.getEmail(), response.getEmail());
                    assertEquals(client.getClientType(), response.getClientType());
                });

        verify(updateClientService).execute(anyLong(), any(Client.class));
    }

    @Test
    @DisplayName("Eliminar cliente")
    void deleteClient() {
        doNothing().when(deleteClientService).execute(anyLong());

        webTestClient.delete()
                .uri("/clients/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        verify(deleteClientService).execute(anyLong());
    }
}
