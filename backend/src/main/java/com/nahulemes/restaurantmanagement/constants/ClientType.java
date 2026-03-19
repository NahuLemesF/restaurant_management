package com.nahulemes.restaurantmanagement.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ClientType {
    COMMON("Comun"),
    FREQUENT("Frecuente");

    private final String name;

    ClientType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

}

