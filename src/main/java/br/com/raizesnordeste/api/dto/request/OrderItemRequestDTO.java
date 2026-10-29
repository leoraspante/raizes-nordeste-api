package br.com.raizesnordeste.api.dto.request;

import jakarta.validation.constraints.*;

public record OrderItemRequestDTO(

        @NotNull(message = "O ID do produto é obrigatório.")
        Long productId,

        @NotNull(message = "A quantidade de produto é obrigatória.")
        @Positive(message = "A quantidade deve ser maior que zero.")
        Integer quantity
) {
}
