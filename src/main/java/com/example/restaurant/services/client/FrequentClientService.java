package com.example.restaurant.services.client;

import com.example.restaurant.constants.ClientType;
import com.example.restaurant.constants.EventType;
import com.example.restaurant.models.Client;
import com.example.restaurant.observers.ClientSubject;
import com.example.restaurant.repositories.IClientRepository;
import com.example.restaurant.repositories.IOrderRepository;
import org.springframework.stereotype.Service;

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

    public void updateClientTypeIfFrequent(Long clientId) {
        Long ordersCount = orderRepository.countByClientId(clientId);

        if (ordersCount >= 10) {
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Cliente con el id " + clientId + " no encontrado"));

            if (client.getClientType() != ClientType.FREQUENT) {
                client.setClientType(ClientType.FREQUENT);
                Client saved = clientRepository.save(client);
                clientSubject.notifyObservers(EventType.UPDATE, saved);
            }
        }
    }
}
