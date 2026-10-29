package br.com.raizesnordeste.api.dto.response;

public record UnitResponseDTO(
        Long id,
        String name,
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode,
        Boolean isActive
) {
}
