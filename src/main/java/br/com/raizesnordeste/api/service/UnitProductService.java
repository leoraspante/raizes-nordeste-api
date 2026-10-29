package br.com.raizesnordeste.api.service;

import br.com.raizesnordeste.api.domain.entity.Product;
import br.com.raizesnordeste.api.domain.entity.Unit;
import br.com.raizesnordeste.api.domain.entity.UnitProduct;
import br.com.raizesnordeste.api.domain.exception.BusinessRuleException;
import br.com.raizesnordeste.api.domain.repository.ProductRepository;
import br.com.raizesnordeste.api.domain.repository.UnitProductRepository;
import br.com.raizesnordeste.api.domain.repository.UnitRepository;
import br.com.raizesnordeste.api.dto.request.UnitProductRequestDTO;
import br.com.raizesnordeste.api.dto.response.UnitProductResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UnitProductService {

    private final UnitProductRepository unitProductRepository;
    private final UnitRepository unitRepository;
    private final ProductRepository productRepository;

    public UnitProductService(UnitProductRepository unitProductRepository,
                              UnitRepository unitRepository,
                              ProductRepository productRepository) {
        this.unitProductRepository = unitProductRepository;
        this.unitRepository = unitRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public UnitProductResponseDTO create(UnitProductRequestDTO request) {

        // Valida se o determinado produto já possui vínculo com a filial.
        if (unitProductRepository.existsByUnitIdAndProductId(request.unitId(),  request.productId())) {
            throw new BusinessRuleException("Este produto já está vinculado a esta filial.");
        }

        // Verifica se a filial já existe.
        Unit unit = unitRepository.findById(request.unitId())
                .orElseThrow(() -> new BusinessRuleException("Filial não encontrada no sistema."));

        // Verifica se o produto o produto já existe.
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new BusinessRuleException("Produto não encontrado no sistema."));

        // Se todas as verificações anteriores foram atendidas, o vínculo é realizado.
        UnitProduct unitProduct = new UnitProduct();
        unitProduct.setUnit(unit);
        unitProduct.setProduct(product);
        unitProduct.setIsAvailable(request.isAvailable());

        UnitProduct saved = unitProductRepository.save(unitProduct);

        return new UnitProductResponseDTO(
                saved.getId(),
                saved.getUnit().getId(),
                saved.getProduct().getId(),
                saved.getIsAvailable()
        );
    }

    @Transactional
    public List<UnitProductResponseDTO> findAll() {
        return unitProductRepository.findAll().stream()
                .map(up -> new UnitProductResponseDTO(
                        up.getId(),
                        up.getUnit().getId(),
                        up.getProduct().getId(),
                        up.getIsAvailable()
                ))
                .toList();
    }
}
