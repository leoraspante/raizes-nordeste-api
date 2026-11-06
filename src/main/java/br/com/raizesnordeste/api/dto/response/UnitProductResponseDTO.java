package br.com.raizesnordeste.api.dto.response;

/**
 * Reflete a visão regionalizada do catálogo de produtos.
 * Retorna a disponibilidade e a quantidade física (estoque) atualizada na filial selecionada,
 * vital para a interface de usuário bloquear a venda de itens esgotados em tempo real.
 */

public record UnitProductResponseDTO(
        Long id,
        Long unitId,
        Long productId,
        Boolean isAvailable,
        Integer quantity
) {
}
