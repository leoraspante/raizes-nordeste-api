package br.com.raizesnordeste.api.controller;

import br.com.raizesnordeste.api.dto.request.ProductRequestDTO;
import br.com.raizesnordeste.api.dto.response.ProductResponseDTO;
import br.com.raizesnordeste.api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

/**
 * Endpoint dedicado à manutenção do Catálogo Global (itens mestre),
 * que servem de base para a distribuição de estoque regional.
 */

@RestController
@RequestMapping("/products")
@Tag(name = "Produtos", description = "Catálogo global de itens do restaurante")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo produto", description = "Adiciona um novo item ao catálogo global da rede (este item precisará ser vinculado a uma filial para ser vendido).")
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductRequestDTO request) {
        ProductResponseDTO response = productService.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    @Operation(summary = "Lista todos os produtos", description = "Retorna o catálogo global completo de produtos disponíveis na rede.")
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }
}
