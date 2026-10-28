package br.com.raizesnordeste.api.dto.request;

import jakarta.validation.constraints.*;

public record UserRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        String name,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve possuir no mínimo 6 caracteres")
        String password
) {
}
