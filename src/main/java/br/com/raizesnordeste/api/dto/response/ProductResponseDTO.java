package br.com.raizesnordeste.api.dto.response;

import br.com.raizesnordeste.api.domain.enums.Category;
import java.math.BigDecimal;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        Category category,
        BigDecimal price
) {
}
