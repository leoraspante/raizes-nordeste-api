package br.com.raizesnordeste.api.controller;

import br.com.raizesnordeste.api.dto.request.UserRequestDTO;
import br.com.raizesnordeste.api.dto.response.UserResponseDTO;
import br.com.raizesnordeste.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

/**
 * Endpoint voltado especificamente para a gestão pública de clientes (B2C),
 * separando a criação de contas de consumo da rota de recursos humanos.
 */

@RestController
@RequestMapping("/users")
@Tag(name = "Utilizadores", description = "Gestão de contas e perfis de acesso")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo utilizador", description = "Cria uma conta com perfil CLIENT por padrão. Rota pública.")
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserRequestDTO request) {
        UserResponseDTO response = userService.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    @Operation(summary = "Lista todos os utilizadores", description = "Retorna a lista de contas registadas. Exige token JWT.")
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }
}
