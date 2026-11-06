package br.com.raizesnordeste.api.controller.exception;

import br.com.raizesnordeste.api.domain.exception.BusinessRuleException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Intercetor global de exceções (Controller Advice).
 * Captura exceções lançadas nas camadas de Domínio ou Serviço e traduz
 * para respostas HTTP padronizadas, evitando o vazamento de stack traces
 * (HTTP 500) e garantindo uma integração resiliente com o frontend.
 */

@Hidden
@RestControllerAdvice
public class ResourceExceptionHandler {

    /**
     * Interceta violações de regras de negócio (ex: estoque insuficiente, e-mail duplicado).
     * Converte a falha interna num HTTP 400 (Bad Request) com mensagem humanizada.
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<StandardError> handleBusinessRuleException(BusinessRuleException ex, HttpServletRequest request) {
        StandardError error = new StandardError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de Regra de Negócio",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Interceta falhas de validação de contrato de entrada (Bean Validation / @Valid).
     * Mapeia múltiplos erros de atributos (ex: e-mail inválido, CEP incorreto) num
     * HTTP 422 (Unprocessable Content), detalhando exatamente o que o cliente deve corrigir.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        // Mapeia todos os erros contidos no payload e os concatena em uma única string.
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        StandardError error = new StandardError(
                Instant.now(),
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                "Erro de Validação de Dados",
                errorMessage,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(error);
    }
}
