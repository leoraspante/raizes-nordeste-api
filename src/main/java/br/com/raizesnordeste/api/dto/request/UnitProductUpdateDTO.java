package br.com.raizesnordeste.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UnitProductUpdateDTO(

        @NotNull(message = "A quantidade é obrigatória.")
        @Min(value = 0, message = "A quantidade não pode ser negativa.")
        Integer quantity,

        @NotNull(message = "O status de disponibilidade é obrigatório.")
        Boolean isAvailable
) {
}
