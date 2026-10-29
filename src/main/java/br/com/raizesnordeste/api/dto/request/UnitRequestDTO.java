package br.com.raizesnordeste.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UnitRequestDTO(

        @NotBlank(message = "O nome da filial é obrigatório.")
        String name,

        @NotBlank(message = "O nome da rua é obrigatório.")
        String street,

        @NotBlank(message = "O número do endreço é obrigatório.")
        String number,

        String complement,

        @NotBlank(message = "O nome do bairro é obrigatório.")
        String neighborhood,

        @NotBlank(message = "O nome da cidade é obrigatório.")
        String city,

        @NotBlank(message = "O nome do estado é obrigatório.")
        String state,

        @NotBlank(message = "O CEP é obrigatório.")
        String zipCode
) {
}
