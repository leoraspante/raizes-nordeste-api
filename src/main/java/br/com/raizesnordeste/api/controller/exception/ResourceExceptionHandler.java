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

@Hidden
@RestControllerAdvice
public class ResourceExceptionHandler {

    // Trata as regras de negócio (ex: Cadastro de usuário com e-mail duplicado) - Retorna erro 400.
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

    // Trata erros de digitação capturados pelo @Valid (ex: e-mail inválido) - Retorna erro 422.
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
