package com.nahulemes.restaurantmanagement.observers;

import com.nahulemes.restaurantmanagement.models.Menu;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class MenuSubject extends GenericSubject<Menu> {

    private final MenuNotificationObserver menuNotificationObserver;

    public MenuSubject(MenuNotificationObserver menuNotificationObserver) {
        this.menuNotificationObserver = menuNotificationObserver;
    }

    @PostConstruct
    public void init() {
        addObserver(menuNotificationObserver);
    }
}
