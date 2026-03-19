package com.nahulemes.restaurantmanagement.constants;

import com.fasterxml.jackson.annotation.JsonValue;


public enum DishType {
    COMMON("Comun"),
    POPULAR("Popular");

    private final String name;

    DishType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
