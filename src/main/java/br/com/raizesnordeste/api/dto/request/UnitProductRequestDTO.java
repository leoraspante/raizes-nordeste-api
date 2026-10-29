package br.com.raizesnordeste.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record UnitProductRequestDTO(

        @NotNull(message = "O ID da filial é obrigatório.")
        Long unitId,

        @NotNull(message = "O ID do produto é obrigatório.")
        Long productId,

        @NotNull(message = "O estatus de disponibilidade é obrigatório.")
        Boolean isAvailable
) {
}
