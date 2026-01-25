package com.nahulemes.restaurantmanagement.observers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DishSubjectTest {

    @Test
    void testDishSubjectInstantiation() {
        DishSubject dishSubject = new DishSubject();
        assertNotNull(dishSubject);
    }
}
