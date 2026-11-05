package br.com.raizesnordeste.api.domain.enums;

/**
 * Identifica o canal de origem e a modalidade de atendimento da venda.
 * O tipo de serviço impacta diretamente na máquina de regras do negócio,
 * como a isenção automática da taxa de entrega para modalidades físicas.
 */

public enum ServiceType {
    ONLINE,
    IN_PERSON,      // Atendimentos realizados no balcão.
    PHONE,
    KIOSK           // Atendimentos realizados no Totem de auto-atendimento.
}
