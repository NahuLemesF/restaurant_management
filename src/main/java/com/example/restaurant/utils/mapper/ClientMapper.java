package com.example.restaurant.utils.mapper;

import com.example.restaurant.dto.client.ClientRequestDTO;
import com.example.restaurant.dto.client.ClientResponseDTO;
import com.example.restaurant.models.Client;

public class ClientMapper {

    public static ClientResponseDTO toDto(Client client) {
        return new ClientResponseDTO(
                client.getId(),
                client.getName(),
                client.getLastName(),
                client.getEmail(),
                client.getClientType().getName()
        );
    }

    public static Client toEntity(ClientRequestDTO dto) {
        Client client = new Client();
        client.setName(dto.name());
        client.setLastName(dto.lastName());
        client.setEmail(dto.email());
        return client;
    }
}
