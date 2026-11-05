package br.com.raizesnordeste.api.domain.enums;

/**
 * Define as modalidades de liquidação financeira aceitas pelo sistema
 * no momento do fechamento do pedido.
 */

public enum PaymentMethod {
    PIX,
    CREDIT_CARD,
    DEBIT_CARD,
    CASH
}
