package br.com.raizesnordeste.api.dto.request;

import br.com.raizesnordeste.api.domain.enums.PaymentMethod;
import br.com.raizesnordeste.api.domain.enums.ServiceType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record OrderRequestDTO(

        @NotNull(message = "O ID do usuário é obrigatório.")
        Long userId,

        @NotNull(message = "O ID da filial é obrigatório.")
        Long unitId,

        Long deliveryAddressId,

        @NotNull(message = "A forma de pagamento deve ser informada.")
        PaymentMethod paymentMethod,

        @NotNull(message = "O tipo de atendimento deve ser informado.")
        ServiceType serviceType,

        @NotEmpty(message = "O pedido deve conter pelo menos um item.")
        List<@Valid OrderItemRequestDTO> items
) {
}
