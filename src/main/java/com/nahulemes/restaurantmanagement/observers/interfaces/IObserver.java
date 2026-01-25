package com.nahulemes.restaurantmanagement.observers.interfaces;

import com.nahulemes.restaurantmanagement.constants.EventType;

public interface IObserver<T> {
    void update(EventType eventType, T entity);
}
