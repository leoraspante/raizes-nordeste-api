package br.com.raizesnordeste.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AddressRequestDTO(
        @NotNull(message = "O ID do usuário é obrigatório.")
        Long userId,

        @NotBlank(message = "A rua é obrigatória.")
        String street,

        @NotBlank(message = "O número é obrigatório.")
        String number,

        String complement,

        @NotBlank(message = "O bairro é obrigatório.")
        String neighborhood,

        @NotBlank(message = "A cidade é obrigatória.")
        String city,

        @NotBlank(message = "O estado é obrigatório.")
        String state,

        @NotBlank(message = "O CEP é obrigatório.")
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "O CEP deve seguir o formato XXXXX-XXX.")
        String zipCode
) {
}
