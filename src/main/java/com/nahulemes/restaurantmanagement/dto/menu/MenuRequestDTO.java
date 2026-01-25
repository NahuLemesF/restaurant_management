package com.nahulemes.restaurantmanagement.dto.menu;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record MenuRequestDTO(
        @NotBlank(message = "El nombre del menú es obligatorio")
        String name,

        @NotBlank(message = "La descripción del menú es obligatoria")
        String description,

        List<Long> dishIds
) {
}
