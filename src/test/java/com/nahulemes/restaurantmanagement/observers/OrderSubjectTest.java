package com.nahulemes.restaurantmanagement.observers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderSubjectTest {

    @Test
    void testOrderSubjectInstantiation() {
        OrderSubject orderSubject = new OrderSubject();
        assertNotNull(orderSubject);
    }
}
