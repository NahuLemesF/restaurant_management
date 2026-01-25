package com.nahulemes.restaurantmanagement.services.client;

import com.nahulemes.restaurantmanagement.dto.client.ClientRequestDTO;
import com.nahulemes.restaurantmanagement.models.Client;

import java.util.List;

public interface ClientService {
    Client create(ClientRequestDTO dto);
    Client getById(Long id);
    List<Client> getAll();
    void delete(Long id);
    Client update(Long id, ClientRequestDTO dto);
}
