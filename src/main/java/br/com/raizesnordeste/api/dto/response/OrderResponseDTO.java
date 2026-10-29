package br.com.raizesnordeste.api.dto.response;

import br.com.raizesnordeste.api.domain.enums.OrderStatus;
import br.com.raizesnordeste.api.domain.enums.PaymentMethod;
import br.com.raizesnordeste.api.domain.enums.ServiceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long orderId,
        Long userId,
        Long unitId,
        OrderStatus status,
        PaymentMethod paymentMethod,
        ServiceType serviceType,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        List<OrderItemResponseDTO> items
) {
}
