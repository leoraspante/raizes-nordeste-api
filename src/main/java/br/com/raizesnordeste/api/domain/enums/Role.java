package br.com.raizesnordeste.api.domain.enums;

/**
 * Define os perfis de acesso e privilégios da plataforma (Role-Based Access Control).
 * Os valores encapsulam o prefixo 'ROLE_' para garantir compatibilidade
 * nativa e integração direta com o mecanismo de autorização do Spring Security.
 */
public enum Role {
    CLIENT("ROLE_CLIENT"),
    ATTENDANT("ROLE_ATTENDANT"),
    MANAGER("ROLE_MANAGER");

    private final String role;

    //Constructor.
    Role(String role) {
        this.role = role;
    }

    // Getter.
    public String getRole() {
        return role;
    }
}
