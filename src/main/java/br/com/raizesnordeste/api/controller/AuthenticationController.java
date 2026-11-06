package br.com.raizesnordeste.api.controller;

import br.com.raizesnordeste.api.domain.entity.User;
import br.com.raizesnordeste.api.dto.request.LoginRequestDTO;
import br.com.raizesnordeste.api.dto.response.TokenResponseDTO;
import br.com.raizesnordeste.api.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoint público responsável por autenticar utilizadores e gerar o Token JWT
 * necessário para transitar pela malha de segurança Stateless da API.
 */

@RestController
@RequestMapping("/login")
@Tag(name = "Autenticação", description = "Endpoint para login e geração de token JWT")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    @Operation(summary = "Realiza o login", description = "Autentica o utilizador e retorna o token Bearer (JWT) necessário para acessar as rotas protegidas.")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }
}
