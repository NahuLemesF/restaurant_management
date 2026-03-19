package com.nahulemes.restaurantmanagement.observers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class DishSubjectTest {

    @Test
    void testDishSubjectInstantiation() {
        DishNotificationObserver observer = mock(DishNotificationObserver.class);
        DishSubject dishSubject = new DishSubject(observer);
        assertNotNull(dishSubject);
    }
}
