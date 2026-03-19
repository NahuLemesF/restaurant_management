package com.nahulemes.restaurantmanagement.observers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class MenuSubjectTest {

    @Test
    void testMenuSubjectInstantiation() {
        MenuNotificationObserver observer = mock(MenuNotificationObserver.class);
        MenuSubject menuSubject = new MenuSubject(observer);
        assertNotNull(menuSubject);
    }
}
