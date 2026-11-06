package br.com.raizesnordeste.api.controller;

import br.com.raizesnordeste.api.dto.request.UnitProductRequestDTO;
import br.com.raizesnordeste.api.dto.request.UnitProductUpdateDTO;
import br.com.raizesnordeste.api.dto.response.UnitProductResponseDTO;
import br.com.raizesnordeste.api.service.UnitProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

/**
 * Endpoint para a gestão do cruzamento entre Produtos (Catálogo) e Unidades (Filiais).
 * Vital para interfaces de controle de estoque regional e status de venda.
 */

@RestController
@RequestMapping("/unit-products")
@Tag(name = "Estoque e Disponibilidade", description = "Gestão do cardápio específico por filial (vínculo Produto-Unidade)")
public class UnitProductController {

    private final UnitProductService unitProductService;

    public UnitProductController(UnitProductService unitProductService) {
        this.unitProductService = unitProductService;
    }

    @PostMapping
    @Operation(summary = "Vincula um produto a uma filial", description = "Disponibiliza um produto do catálogo global para ser vendido numa filial específica. Impede vinculações duplicadas.")
    public ResponseEntity<UnitProductResponseDTO> create(@RequestBody @Valid UnitProductRequestDTO request) {
        UnitProductResponseDTO response = unitProductService.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    @Operation(summary = "Lista vínculos de produtos", description = "Retorna a relação de quais produtos estão disponíveis em quais filiais.")
    public ResponseEntity<List<UnitProductResponseDTO>> findAll() {
        return ResponseEntity.ok(unitProductService.findAll());
    }

    @PatchMapping("/{unitId}/products/{productId}")
    @Operation(summary = "Atualiza estoque e disponibilidade", description = "Permite atualizar a quantidade em estoque e o status de disponibilidade de um produto em uma filial.")
    public ResponseEntity<UnitProductResponseDTO> updateStock(
            @PathVariable Long unitId,
            @PathVariable Long productId,
            @RequestBody @Valid UnitProductUpdateDTO request) {

        UnitProductResponseDTO response = unitProductService.updateStock(unitId, productId, request);
        return ResponseEntity.ok(response);
    }
}
