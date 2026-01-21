package com.example.restaurant.services.client;

import com.example.restaurant.dto.client.ClientRequestDTO;
import com.example.restaurant.models.Client;

import java.util.List;

public interface ClientService {
    Client create(ClientRequestDTO dto);
    Client getById(Long id);
    List<Client> getAll();
    void delete(Long id);
    Client update(Long id, ClientRequestDTO dto);
}
