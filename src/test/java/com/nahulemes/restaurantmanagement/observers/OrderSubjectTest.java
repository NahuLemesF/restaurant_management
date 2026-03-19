package com.nahulemes.restaurantmanagement.observers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class OrderSubjectTest {

    @Test
    void testOrderSubjectInstantiation() {
        OrderNotificationObserver observer = mock(OrderNotificationObserver.class);
        OrderSubject orderSubject = new OrderSubject(observer);
        assertNotNull(orderSubject);
    }
}
