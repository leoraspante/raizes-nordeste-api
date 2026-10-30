package br.com.raizesnordeste.api.controller;

import br.com.raizesnordeste.api.dto.request.AddressRequestDTO;
import br.com.raizesnordeste.api.dto.response.AddressResponseDTO;
import br.com.raizesnordeste.api.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<AddressResponseDTO> create(@RequestBody @Valid AddressRequestDTO request) {
        AddressResponseDTO response = addressService.create(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<AddressResponseDTO>> findAllByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.findAllByUserId(userId));
    }
}
