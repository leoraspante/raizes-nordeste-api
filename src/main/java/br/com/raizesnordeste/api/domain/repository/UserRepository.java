package br.com.raizesnordeste.api.domain.repository;

import br.com.raizesnordeste.api.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório central de gestão de identidades e acessos (Clientes e Funcionários).
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Recupera os dados do utilizador utilizando o e-mail como chave de negócio (username).
     * Essencial para o processo de autenticação via Token JWT no Spring Security.
     */
    Optional<User> findByEmail(String email);
}
