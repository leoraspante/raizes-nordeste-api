package br.com.raizesnordeste.api.dto.response;

public record AddressResponseDTO(
        Long id,
        Long userId,
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode
) {
}
