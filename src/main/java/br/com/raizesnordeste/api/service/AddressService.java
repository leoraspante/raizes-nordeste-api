package br.com.raizesnordeste.api.service;

import br.com.raizesnordeste.api.domain.entity.Address;
import br.com.raizesnordeste.api.domain.entity.User;
import br.com.raizesnordeste.api.domain.exception.BusinessRuleException;
import br.com.raizesnordeste.api.domain.repository.AddressRepository;
import br.com.raizesnordeste.api.domain.repository.UserRepository;
import br.com.raizesnordeste.api.dto.request.AddressRequestDTO;
import br.com.raizesnordeste.api.dto.response.AddressResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AddressResponseDTO create(AddressRequestDTO request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado."));

        Address address = new Address();
        address.setUser(user);
        address.setStreet(request.street());
        address.setNumber(request.number());
        address.setComplement(request.complement());
        address.setNeighborhood(request.neighborhood());
        address.setCity(request.city());
        address.setState(request.state());
        address.setZipCode(request.zipCode());

        Address savedAddress = addressRepository.save(address);

        return mapToResponse(savedAddress);
    }

    @Transactional(readOnly = true)
    public List<AddressResponseDTO> findAllByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new BusinessRuleException("Usuário não encontrado.");
        }

        return addressRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AddressResponseDTO mapToResponse(Address address) {
        return new AddressResponseDTO(
                address.getId(),
                address.getUser().getId(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getZipCode()
        );
    }
}
