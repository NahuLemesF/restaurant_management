package com.nahulemes.restaurantmanagement.observers;

import com.nahulemes.restaurantmanagement.constants.EventType;
import com.nahulemes.restaurantmanagement.models.Client;
import com.nahulemes.restaurantmanagement.observers.interfaces.IObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClientNotificationObserver implements IObserver<Client> {

    @Override
    public void update(EventType eventType, Client client) {
        switch (eventType) {
            case CREATE -> log.info("Notificación: Nuevo cliente registrado -> {}", client.getName());
            case UPDATE -> log.info("Notificación: Cliente actualizado -> {}", client.getName());
            case DELETE -> log.info("Notificación: Cliente eliminado -> {}", client.getName());
        }
    }
}
