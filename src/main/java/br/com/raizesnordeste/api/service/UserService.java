package br.com.raizesnordeste.api.service;

import br.com.raizesnordeste.api.domain.entity.User;
import br.com.raizesnordeste.api.domain.enums.Role;
import br.com.raizesnordeste.api.domain.exception.BusinessRuleException;
import br.com.raizesnordeste.api.domain.repository.UserRepository;
import br.com.raizesnordeste.api.dto.request.UserRequestDTO;
import br.com.raizesnordeste.api.dto.response.UserResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Injetamos o PasswordEncoder no construtor
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Realiza o cadastro de um novo utilizador na plataforma.
     *
     * Diretrizes de Segurança e Privacidade (LGPD):
     * - Proteção de Dados: A senha enviada pelo utilizador é convertida num hash irreversível
     *   através do algoritmo BCrypt antes da persistência, garantindo que nem mesmo os
     *   administradores da base de dados tenham acesso à credencial real.
     * - Prevenção de Escalada de Privilégios: A role 'CLIENT' é injetada obrigatoriamente no backend.
     *   Isto impede que um invasor manipule o payload do JSON para tentar criar uma conta de gestor.
     */
    @Transactional
    public UserResponseDTO create(UserRequestDTO request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new BusinessRuleException("E-mail já cadastrado no sistema.");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());

        // A senha agora é criptografada antes de ser salva no banco H2
        user.setPassword(passwordEncoder.encode(request.password()));

        user.setRole(Role.CLIENT);

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole()
                ))
                .toList();
    }
}
