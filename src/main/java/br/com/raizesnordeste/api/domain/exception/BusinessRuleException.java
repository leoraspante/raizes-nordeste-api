package br.com.raizesnordeste.api.domain.exception;

/**
 * Exceção customizada para violações de regras de negócio.
 * Utilizada para interromper o fluxo transacional de forma controlada
 * (ex: estoque insuficiente, e-mail duplicado, produto indisponível).
 * Garante que validações do domínio sejam traduzidas em mensagens
 * claras para o frontend, isolando o sistema de exceções genéricas do Java.
 */

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
