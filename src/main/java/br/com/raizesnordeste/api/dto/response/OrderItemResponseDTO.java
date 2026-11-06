package br.com.raizesnordeste.api.dto.response;

import java.math.BigDecimal;

/**
 * Representa a linha de detalhe de um pedido processado.
 * Retorna o preço congelado no momento da compra (unitPrice) e o subtotal calculado no backend,
 * isentando o frontend de recriar regras matemáticas e garantindo a consistência financeira.
 */

public record OrderItemResponseDTO(
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subTotal
) {
}
