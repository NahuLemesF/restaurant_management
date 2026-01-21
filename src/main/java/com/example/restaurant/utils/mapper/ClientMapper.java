package com.example.restaurant.utils.mapper;

import com.example.restaurant.dto.client.ClientRequestDTO;
import com.example.restaurant.dto.client.ClientResponseDTO;
import com.example.restaurant.models.Client;

public class ClientMapper {

    public static ClientResponseDTO toDto(Client client) {
        ClientResponseDTO dto = new ClientResponseDTO();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setClientType(client.getClientType().getName());
        return dto;
    }

    public static Client toEntity(ClientRequestDTO dto) {
        Client client = new Client();
        client.setName(dto.getName());
        client.setLastName(dto.getLastName());
        client.setEmail(dto.getEmail());
        return client;
    }
}
