package br.com.raizesnordeste.api.service;

import br.com.raizesnordeste.api.domain.entity.User;
import br.com.raizesnordeste.api.domain.enums.Role;
import br.com.raizesnordeste.api.domain.exception.BusinessRuleException;
import br.com.raizesnordeste.api.domain.repository.UserRepository;
import br.com.raizesnordeste.api.dto.request.EmployeeRequestDTO;
import br.com.raizesnordeste.api.dto.request.UserRequestDTO;
import br.com.raizesnordeste.api.dto.response.UserResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço central de RH e Gestão de Identidades.
 * Isola rigorosamente a criação de perfis públicos (Clientes)
 * da criação de perfis operacionais (Atendentes e Gerentes).
 */

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Endpoint exposto para registro público (App/Site).
     * Blinda a segurança ao forçar hardcoded a Role CLIENT,
     * prevenindo ataques de escalada de privilégios via manipulação de JSON.
     */
    @Transactional
    public UserResponseDTO create(UserRequestDTO request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new BusinessRuleException("E-mail já cadastrado no sistema.");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        user.setRole(Role.CLIENT);
        user.setLoyaltyPoints(0);

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    /**
     * Rota isolada para o RH (uso restrito a Managers).
     * Permite delegar explicitamente o nível de acesso (Role) no payload.
     */
    @Transactional
    public UserResponseDTO createEmployee(EmployeeRequestDTO request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new BusinessRuleException("E-mail já cadastrado no sistema.");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        user.setRole(request.role());

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
