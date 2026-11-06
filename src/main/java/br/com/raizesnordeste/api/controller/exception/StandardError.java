package br.com.raizesnordeste.api.controller.exception;

import java.time.Instant;

/**
 * Contrato padrão (Payload) para o retorno de erros da API.
 * Padroniza as respostas de falha, fornecendo ao frontend e a sistemas clientes
 * informações estruturadas de timestamp, status HTTP, mensagem e rastreabilidade da rota.
 */

public record StandardError(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path
) {
}
