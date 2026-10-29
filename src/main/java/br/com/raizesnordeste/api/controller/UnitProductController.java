package br.com.raizesnordeste.api.controller;

import br.com.raizesnordeste.api.dto.request.UnitProductRequestDTO;
import br.com.raizesnordeste.api.dto.response.UnitProductResponseDTO;
import br.com.raizesnordeste.api.service.UnitProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/unit-products")
public class UnitProductController {

    private final UnitProductService unitProductService;

    public UnitProductController(UnitProductService unitProductService) {
        this.unitProductService = unitProductService;
    }

    @PostMapping
    public ResponseEntity<UnitProductResponseDTO> create(@RequestBody @Valid UnitProductRequestDTO request) {
        UnitProductResponseDTO response = unitProductService.create(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UnitProductResponseDTO>> findAll() {
        return ResponseEntity.ok(unitProductService.findAll());
    }
}
