package br.com.raizesnordeste.api.domain.enums;

/**
 * Enum responsável pelo ciclo de vida operacional de um pedido.
 */

public enum OrderStatus {
    AWAITING_PAYMENT,
    PAID,
    IN_PREPARATION,
    READY_FOR_PICKUP,
    OUT_FOR_DELIVERY,
    DELIVERED,
    RETURNED_TO_STORE,
    CANCELED
}
