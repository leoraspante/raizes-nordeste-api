package br.com.raizesnordeste.api.dto.request;

import br.com.raizesnordeste.api.domain.enums.Category;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductRequestDTO(

        @NotBlank(message = "O nome do produto é obrigatório.")
        String name,

        String description,

        @NotNull(message = "A categoria é obrigatória.")
        Category category,

        @NotNull(message = "O preço é obrigatório.")
        @PositiveOrZero(message = "O preço não pode ser negativo.")
        BigDecimal price
) {
}
