package br.com.raizesnordeste.api.controller;

import br.com.raizesnordeste.api.dto.request.OrderRequestDTO;
import br.com.raizesnordeste.api.dto.response.OrderResponseDTO;
import br.com.raizesnordeste.api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Pedidos", description = "Gestão do motor de vendas, ciclo de vida e pagamentos")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Cria um novo pedido", description = "Regista um pedido, calcula o total e aplica regras de fidelidade e taxa de entrega (R$ 10,00 para pedidos ONLINE).")
    public ResponseEntity<OrderResponseDTO> create(@RequestBody @Valid OrderRequestDTO resquest) {
        OrderResponseDTO response = orderService.create(resquest);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.orderId())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    @Operation(summary = "Lista todos os pedidos", description = "Retorna o histórico de pedidos da rede com os respetivos preços originais (congelados no momento da compra).")
    public ResponseEntity<List<OrderResponseDTO>> findAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @PatchMapping("/{id}/payment")
    @Operation(summary = "Processa webhook de pagamento (Mock)", description = "Altera o status do pedido para PAGO ou CANCELADO através da simulação de um gateway externo.")
    public ResponseEntity<br.com.raizesnordeste.api.dto.response.OrderResponseDTO> processPayment(
            @PathVariable Long id,
            @RequestBody @Valid br.com.raizesnordeste.api.dto.request.PaymentMockRequestDTO request) {
        OrderResponseDTO response = orderService.processMockPayment(id, request);
        return ResponseEntity.ok(response);
    }
}
