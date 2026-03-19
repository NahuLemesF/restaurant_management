package com.nahulemes.restaurantmanagement.observers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ClientSubjectTest {

    @Test
    void testClientSubjectInstantiation() {
        ClientNotificationObserver observer = mock(ClientNotificationObserver.class);
        ClientSubject clientSubject = new ClientSubject(observer);
        assertNotNull(clientSubject);
    }
}
