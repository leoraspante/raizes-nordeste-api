package br.com.raizesnordeste.api.dto.request;

import br.com.raizesnordeste.api.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Encapsula o contrato de dados para o cadastro administrativo de funcionários.
 * Isola a camada de criação de contas de RH do fluxo público de clientes,
 * exigindo a definição explícita do nível de acesso (Role) no payload.
 */

public record EmployeeRequestDTO(
        @NotBlank(message = "O nome é obrigatório.")
        String name,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Formato de e-mail inválido.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        String password,

        @NotNull(message = "O perfil de acesso é obrigatório.")
        Role role
) {
}
