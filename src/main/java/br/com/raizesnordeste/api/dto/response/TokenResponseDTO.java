package br.com.raizesnordeste.api.dto.response;

/**
 * Encapsula o token JWT (JSON Web Token) gerado após uma autenticação bem-sucedida.
 * Contrato utilizado pelo frontend para armazenar a sessão de forma stateless e
 * assinar as requisições subsequentes no cabeçalho Authorization.
 */

public record TokenResponseDTO(String token) {
}
