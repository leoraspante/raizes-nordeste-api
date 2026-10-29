package br.com.raizesnordeste.api.dto.response;

public record UnitProductResponseDTO(
        Long id,
        Long unitId,
        Long productId,
        Boolean isAvailable
) {
}
