package com.nahulemes.restaurantmanagement.observers;

import com.nahulemes.restaurantmanagement.constants.EventType;
import com.nahulemes.restaurantmanagement.models.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ClientNotificationObserverTest {

    private ClientNotificationObserver clientNotificationObserver;

    @BeforeEach
    void setUp() {
        clientNotificationObserver = new ClientNotificationObserver();
    }

    @Test
    void testUpdateCreate() {
        Client client = new Client();
        client.setName("John");

        assertDoesNotThrow(() -> clientNotificationObserver.update(EventType.CREATE, client));
    }

    @Test
    void testUpdateUpdate() {
        Client client = new Client();
        client.setName("John");

        assertDoesNotThrow(() -> clientNotificationObserver.update(EventType.UPDATE, client));
    }

    @Test
    void testUpdateDelete() {
        Client client = new Client();
        client.setName("John");

        assertDoesNotThrow(() -> clientNotificationObserver.update(EventType.DELETE, client));
    }
}
