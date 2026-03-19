package com.nahulemes.restaurantmanagement.services.interfaces;

import com.nahulemes.restaurantmanagement.dto.client.ClientRequestDTO;
import com.nahulemes.restaurantmanagement.models.Client;

import java.util.List;

/**
 * Puerto de entrada (inbound port) para las operaciones de Clientes.
 * Los controladores dependen ÚNICAMENTE de esta interfaz (DIP).
 */
public interface IClientService {
    Client create(ClientRequestDTO dto);
    Client getById(Long id);
    List<Client> getAll();
    void delete(Long id);
    Client update(Long id, ClientRequestDTO dto);
}
