package br.com.raizesnordeste.api.domain.enums;

/**
 * Representa a máquina de estados (State Machine) e o ciclo de vida operacional de um pedido,
 * desde a sua captação financeira até à entrega final, devolução ou cancelamento.
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
