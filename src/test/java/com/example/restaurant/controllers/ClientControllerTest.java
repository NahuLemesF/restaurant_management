package com.example.restaurant.controllers;

import com.example.restaurant.constants.ClientType;
import com.example.restaurant.dto.client.ClientRequestDTO;
import com.example.restaurant.models.Client;
import com.example.restaurant.services.client.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


class ClientControllerTest {

    private MockMvc mockMvc;
    private ClientService clientService;
    private ObjectMapper objectMapper;

    private Client client;

    @BeforeEach
    void setUp() {
        clientService = mock(ClientService.class);
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(new ClientController(clientService)).build();

        client = new Client(1L, "Martin", "Garmendia", "holasoymartin@example.com", ClientType.COMMON);
    }

    @Test
    @DisplayName("Agregar cliente")
    void addClient() throws Exception {
        when(clientService.create(any(ClientRequestDTO.class))).thenReturn(client);

        ClientRequestDTO requestDTO = new ClientRequestDTO();
        requestDTO.setName("Martin");
        requestDTO.setLastName("Garmendia");
        requestDTO.setEmail("holasoymartin@example.com");

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(client.getName()))
                .andExpect(jsonPath("$.lastName").value(client.getLastName()))
                .andExpect(jsonPath("$.email").value(client.getEmail()));

        verify(clientService).create(any(ClientRequestDTO.class));
    }

    @Test
    @DisplayName("Obtener cliente por ID")
    void getClientById() throws Exception {
        when(clientService.getById(anyLong())).thenReturn(client);

        mockMvc.perform(get("/clients/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(client.getName()))
                .andExpect(jsonPath("$.lastName").value(client.getLastName()))
                .andExpect(jsonPath("$.email").value(client.getEmail()));

        verify(clientService).getById(anyLong());
    }

    @Test
    @DisplayName("Listar todos los clientes")
    void getAllClients() throws Exception {
        List<Client> clients = List.of(
                client,
                new Client(2L, "Nahuel", "Lemes", "nahulemes@example.com", ClientType.FREQUENT)
        );

        when(clientService.getAll()).thenReturn(clients);

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Martin"))
                .andExpect(jsonPath("$[1].name").value("Nahuel"));

        verify(clientService).getAll();
    }

    @Test
    @DisplayName("Actualizar cliente")
    void updateClient() throws Exception {
        when(clientService.update(anyLong(), any(ClientRequestDTO.class))).thenReturn(client);

        ClientRequestDTO requestDTO = new ClientRequestDTO();
        requestDTO.setName("Martin");
        requestDTO.setLastName("Garmendia");
        requestDTO.setEmail("holasoymartin@example.com");

        mockMvc.perform(put("/clients/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(client.getName()))
                .andExpect(jsonPath("$.lastName").value(client.getLastName()))
                .andExpect(jsonPath("$.email").value(client.getEmail()));

        verify(clientService).update(anyLong(), any(ClientRequestDTO.class));
    }

    @Test
    @DisplayName("Eliminar cliente")
    void deleteClient() throws Exception {
        doNothing().when(clientService).delete(anyLong());

        mockMvc.perform(delete("/clients/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(clientService).delete(anyLong());
    }
}
