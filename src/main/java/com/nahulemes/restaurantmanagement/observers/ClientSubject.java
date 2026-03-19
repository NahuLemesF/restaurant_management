package com.nahulemes.restaurantmanagement.observers;

import com.nahulemes.restaurantmanagement.models.Client;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ClientSubject extends GenericSubject<Client> {

    private final ClientNotificationObserver clientNotificationObserver;

    public ClientSubject(ClientNotificationObserver clientNotificationObserver) {
        this.clientNotificationObserver = clientNotificationObserver;
    }

    @PostConstruct
    public void init() {
        addObserver(clientNotificationObserver);
    }
}
