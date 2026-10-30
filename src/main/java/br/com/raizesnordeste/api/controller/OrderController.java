package br.com.raizesnordeste.api.controller;

import br.com.raizesnordeste.api.dto.request.OrderRequestDTO;
import br.com.raizesnordeste.api.dto.response.OrderResponseDTO;
import br.com.raizesnordeste.api.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody @Valid OrderRequestDTO resquest) {
        OrderResponseDTO response = orderService.create(resquest);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.orderId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> findAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @PatchMapping("/{id}/payment")
    public ResponseEntity<br.com.raizesnordeste.api.dto.response.OrderResponseDTO> processPayment(
            @PathVariable Long id,
            @RequestBody @Valid br.com.raizesnordeste.api.dto.request.PaymentMockRequestDTO request) {

        br.com.raizesnordeste.api.dto.response.OrderResponseDTO response = orderService.processMockPayment(id, request);
        return ResponseEntity.ok(response);
    }
}
