package com.nahulemes.restaurantmanagement.observers;

import com.nahulemes.restaurantmanagement.models.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientSubject extends GenericSubject<Client> {

    public ClientSubject() {
        super();
    }
}
