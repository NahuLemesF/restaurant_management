package com.nahulemes.restaurantmanagement.observers;

import com.nahulemes.restaurantmanagement.models.Menu;
import org.springframework.stereotype.Component;

@Component
public class MenuSubject extends GenericSubject<Menu> {

    public MenuSubject() {
        super();
    }
}
