package br.com.raizesnordeste.api.controller;

import br.com.raizesnordeste.api.dto.request.UnitRequestDTO;
import br.com.raizesnordeste.api.dto.response.UnitResponseDTO;
import br.com.raizesnordeste.api.service.UnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/units")
@Tag(name = "Filiais", description = "Gestão das unidades físicas da rede Raízes do Nordeste")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @PostMapping
    @Operation(summary = "Cadastra uma nova filial", description = "Cria uma nova unidade física da rede, validando previamente se o nome já se encontra em uso.")
    public ResponseEntity<UnitResponseDTO> create(@RequestBody @Valid UnitRequestDTO request) {
        UnitResponseDTO response = unitService.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    @Operation(summary = "Lista todas as filiais", description = "Retorna todas as unidades registadas no sistema e os seus respetivos endereços de operação.")
    public ResponseEntity<List<UnitResponseDTO>> findAll() {
        return ResponseEntity.ok(unitService.findAll());
    }
}
