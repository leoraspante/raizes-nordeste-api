package br.com.raizesnordeste.api.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * Contrato de simulação (Mock) para integrações financeiras (ex: gateways de pagamento).
 * Permite que o frontend teste os fluxos de sucesso (aprovação) e falha (recusa)
 * no avanço da máquina de estados do pedido (OrderStatus).
 */

public record PaymentMockRequestDTO(

        @NotNull(message = "A decisão de aprovação (true/false) é obrigatória.")
        Boolean isApproved
) {
}
