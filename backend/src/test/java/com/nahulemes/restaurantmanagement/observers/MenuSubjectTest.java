package com.nahulemes.restaurantmanagement.observers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MenuSubjectTest {

    @Test
    void testMenuSubjectInstantiation() {
        MenuSubject menuSubject = new MenuSubject();
        assertNotNull(menuSubject);
    }
}
