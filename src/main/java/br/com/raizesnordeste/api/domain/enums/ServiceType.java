package br.com.raizesnordeste.api.domain.enums;

/**
 * Enum responsável pelas opções de atendimento.
 */

public enum ServiceType {
    ONLINE,
    IN_PERSON,      // Atendimentos realizados no balcão.
    PHONE,
    KIOSK           // Atendimentos realizados no Totem de auto-atendimento.
}
