package com.nahulemes.restaurantmanagement.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClientRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe ser válido")
        String email
) {
}
