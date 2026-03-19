package com.nahulemes.restaurantmanagement.services.client;

import com.nahulemes.restaurantmanagement.constants.ClientType;
import com.nahulemes.restaurantmanagement.constants.EventType;
import com.nahulemes.restaurantmanagement.exception.ResourceNotFoundException;
import com.nahulemes.restaurantmanagement.models.Client;
import com.nahulemes.restaurantmanagement.observers.ClientSubject;
import com.nahulemes.restaurantmanagement.repositories.IClientRepository;
import com.nahulemes.restaurantmanagement.repositories.IOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FrequentClientService {

    private final IOrderRepository orderRepository;
    private final IClientRepository clientRepository;
    private final ClientSubject clientSubject;

    public FrequentClientService(IOrderRepository orderRepository,
                                 IClientRepository clientRepository,
                                 ClientSubject clientSubject) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.clientSubject = clientSubject;
    }

    @Transactional
    public void updateClientTypeIfFrequent(Long clientId) {
        Long ordersCount = orderRepository.countByClientId(clientId);

        if (ordersCount >= 10) {
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", clientId));

            if (client.getClientType() != ClientType.FREQUENT) {
                client.setClientType(ClientType.FREQUENT);
                Client saved = clientRepository.save(client);
                clientSubject.notifyObservers(EventType.UPDATE, saved);
            }
        }
    }
}
