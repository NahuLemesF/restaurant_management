package com.nahulemes.restaurantmanagement.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequestDTO(
        @NotNull(message = "El ID del cliente es obligatorio")
        Long clientId,

        @NotNull(message = "La lista de platos es obligatoria")
        @NotEmpty(message = "La orden debe contener al menos un plato")
        List<Long> dishIds
) {
}
