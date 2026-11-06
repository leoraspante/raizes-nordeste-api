package br.com.raizesnordeste.api.dto.response;

import br.com.raizesnordeste.api.domain.enums.Role;

/**
 * Contrato de saída para os dados do usuário.
 * Oculta intencionalmente dados sensíveis da entidade principal (como a senha encriptada),
 * aplicando o padrão de Data Masking/Omission para garantir a segurança da API.
 */

public record UserResponseDTO(

        Long id,
        String name,
        String email,
        Role role
) {
}
