package com.nahulemes.restaurantmanagement.observers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClientSubjectTest {

    @Test
    void testClientSubjectInstantiation() {
        ClientSubject clientSubject = new ClientSubject();
        assertNotNull(clientSubject);
    }
}
