package br.com.raizesnordeste.api.service;

import br.com.raizesnordeste.api.domain.entity.User;
import br.com.raizesnordeste.api.domain.enums.Role;
import br.com.raizesnordeste.api.domain.repository.UserRepository;
import br.com.raizesnordeste.api.dto.request.UserRequestDTO;
import br.com.raizesnordeste.api.dto.response.UserResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDTO create(UserRequestDTO request) {

        // Verifica se o e-mail informado já está sendo utilizado em outro cadastro.
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado no sistema.");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());

        // Garante que o perfil cadastrado seja sempre do tipo "ClIENT".
        user.setRole(Role.CLIENT);

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }
}
