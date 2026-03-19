package com.nahulemes.restaurantmanagement.services.client;

import com.nahulemes.restaurantmanagement.constants.EventType;
import com.nahulemes.restaurantmanagement.dto.client.ClientRequestDTO;
import com.nahulemes.restaurantmanagement.exception.ResourceNotFoundException;
import com.nahulemes.restaurantmanagement.models.Client;
import com.nahulemes.restaurantmanagement.observers.ClientSubject;
import com.nahulemes.restaurantmanagement.repositories.IClientRepository;
import com.nahulemes.restaurantmanagement.utils.mapper.ClientMapper;
import com.nahulemes.restaurantmanagement.services.interfaces.IClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;
    private final ClientSubject clientSubject;

    public ClientServiceImpl(IClientRepository clientRepository, ClientSubject clientSubject) {
        this.clientRepository = clientRepository;
        this.clientSubject = clientSubject;
    }

    @Override
    @Transactional
    public Client create(ClientRequestDTO dto) {
        Client newClient = ClientMapper.toEntity(dto);
        Client createdClient = clientRepository.save(newClient);
        clientSubject.notifyObservers(EventType.CREATE, createdClient);
        return createdClient;
    }

    @Override
    @Transactional(readOnly = true)
    public Client getById(Long id) {
        return requireClient(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Client clientToDelete = requireClient(id);
        clientRepository.delete(clientToDelete);
        clientSubject.notifyObservers(EventType.DELETE, clientToDelete);
    }

    @Override
    @Transactional
    public Client update(Long id, ClientRequestDTO dto) {
        Client existingClient = requireClient(id);

        existingClient.setName(dto.name());
        existingClient.setLastName(dto.lastName());
        existingClient.setEmail(dto.email());

        Client updatedClient = clientRepository.save(existingClient);
        clientSubject.notifyObservers(EventType.UPDATE, updatedClient);
        return updatedClient;
    }

    private Client requireClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", id));
    }
}
