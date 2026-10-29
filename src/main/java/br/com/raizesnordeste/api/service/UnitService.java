package br.com.raizesnordeste.api.service;

import br.com.raizesnordeste.api.domain.entity.Unit;
import br.com.raizesnordeste.api.domain.exception.BusinessRuleException;
import br.com.raizesnordeste.api.domain.repository.UnitRepository;
import br.com.raizesnordeste.api.dto.request.UnitRequestDTO;
import br.com.raizesnordeste.api.dto.response.UnitResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnitService {

    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @Transactional
    public UnitResponseDTO create(UnitRequestDTO request) {

        // Verificase uma filial já existe com o nome cadastrado antes de qualquer operação.
        if (unitRepository.existsByName(request.name())) {
            throw new BusinessRuleException("Já existe uma filial cadastrada com este nome.");
        }

        Unit unit = new Unit();
        unit.setName(request.name());
        unit.setStreet(request.street());
        unit.setNumber(request.number());
        unit.setComplement(request.complement());
        unit.setNeighborhood(request.neighborhood());
        unit.setCity(request.city());
        unit.setState(request.state());
        unit.setZipCode(request.zipCode());

        // Garantia de que a filial recém-criada inicie como ativa.
        unit.setIsActive(true);

        Unit savedUnit = unitRepository.save(unit);

        return new UnitResponseDTO(
                savedUnit.getId(), savedUnit.getName(), savedUnit.getStreet(),
                savedUnit.getNumber(), savedUnit.getComplement(), savedUnit.getNeighborhood(),
                savedUnit.getCity(), savedUnit.getState(), savedUnit.getZipCode(),
                savedUnit.getIsActive()
        );
    }

    @Transactional(readOnly = true)
    public List<UnitResponseDTO> findAll() {
        return unitRepository.findAll().stream()
                .map(unit -> new UnitResponseDTO(
                        unit.getId(), unit.getName(), unit.getStreet(),
                        unit.getNumber(), unit.getComplement(), unit.getNeighborhood(),
                        unit.getCity(), unit.getState(), unit.getZipCode(),
                        unit.getIsActive()
                ))
                .collect(Collectors.toList());
    }
}
