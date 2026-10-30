package br.com.raizesnordeste.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record PaymentMockRequestDTO(

        @NotNull(message = "A decisão de aprovação (true/false) é obrigatória.")
        Boolean isApproved
) {
}
