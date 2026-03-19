package com.nahulemes.restaurantmanagement.observers;

import com.nahulemes.restaurantmanagement.constants.EventType;
import com.nahulemes.restaurantmanagement.observers.interfaces.IObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GenericSubjectTest {

    private GenericSubject<Object> genericSubject;
    private IObserver<Object> observer1;
    private IObserver<Object> observer2;

    @BeforeEach
    void setUp() {
        genericSubject = new GenericSubject<>();
        observer1 = mock(IObserver.class);
        observer2 = mock(IObserver.class);
        genericSubject.addObserver(observer1);
        genericSubject.addObserver(observer2);
    }

    @Test
    void testNotifyObservers() {
        Object entity = new Object();
        EventType eventType = EventType.CREATE;

        genericSubject.notifyObservers(eventType, entity);

        verify(observer1).update(eventType, entity);
        verify(observer2).update(eventType, entity);
    }
}