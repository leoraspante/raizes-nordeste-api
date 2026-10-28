package br.com.raizesnordeste.api.dto.response;

import br.com.raizesnordeste.api.domain.enums.Role;

public record UserResponseDTO(

        Long id,
        String name,
        String email,
        Role role
) {
}
