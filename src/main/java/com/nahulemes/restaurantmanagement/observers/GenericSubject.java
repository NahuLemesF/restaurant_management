package com.nahulemes.restaurantmanagement.observers;

import com.nahulemes.restaurantmanagement.constants.EventType;
import com.nahulemes.restaurantmanagement.observers.interfaces.IObserver;

import java.util.ArrayList;
import java.util.List;

public class GenericSubject<T> {

    private final List<IObserver<T>> observers = new ArrayList<>();

    public void notifyObservers(EventType eventType, T entity) {
        for (IObserver<T> observer : observers) {
            observer.update(eventType, entity);
        }
    }

    public void addObserver(IObserver<T> observer) {
        observers.add(observer);
    }
}
