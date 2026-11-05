package br.com.raizesnordeste.api.domain.enums;

/**
 * Enum responsável pelos perfis de acesso ao sistema.
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
