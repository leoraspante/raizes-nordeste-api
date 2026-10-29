package br.com.raizesnordeste.api.controller.exception;

import br.com.raizesnordeste.api.dto.request.UnitRequestDTO;
import br.com.raizesnordeste.api.dto.response.UnitResponseDTO;
import br.com.raizesnordeste.api.service.UnitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/units")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @PostMapping
    public ResponseEntity<UnitResponseDTO> create(@RequestBody @Valid UnitRequestDTO request) {
        UnitResponseDTO response = unitService.create(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UnitResponseDTO>> findAll() {
        return ResponseEntity.ok(unitService.findAll());
    }
}
