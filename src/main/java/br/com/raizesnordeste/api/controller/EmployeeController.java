package br.com.raizesnordeste.api.controller;

import br.com.raizesnordeste.api.dto.request.EmployeeRequestDTO;
import br.com.raizesnordeste.api.dto.response.UserResponseDTO;
import br.com.raizesnordeste.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final UserService userService;

    public EmployeeController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(
            summary = "Cadastra novo funcionário",
            description = "Permite que um Gerente (MANAGER) cadastre novos Atendentes (ATTENDANT) ou outros Gerentes na plataforma."
    )
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid EmployeeRequestDTO request) {
        UserResponseDTO response = userService.createEmployee(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }
}
