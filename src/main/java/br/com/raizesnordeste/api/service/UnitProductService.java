package br.com.raizesnordeste.api.service;

import br.com.raizesnordeste.api.domain.entity.Product;
import br.com.raizesnordeste.api.domain.entity.Unit;
import br.com.raizesnordeste.api.domain.entity.UnitProduct;
import br.com.raizesnordeste.api.domain.exception.BusinessRuleException;
import br.com.raizesnordeste.api.domain.repository.ProductRepository;
import br.com.raizesnordeste.api.domain.repository.UnitProductRepository;
import br.com.raizesnordeste.api.domain.repository.UnitRepository;
import br.com.raizesnordeste.api.dto.request.UnitProductRequestDTO;
import br.com.raizesnordeste.api.dto.request.UnitProductUpdateDTO;
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

        if (unitProductRepository.existsByUnitIdAndProductId(request.unitId(), request.productId())) {
            throw new BusinessRuleException("Este produto já está vinculado a esta filial.");
        }

        Unit unit = unitRepository.findById(request.unitId())
                .orElseThrow(() -> new BusinessRuleException("Filial não encontrada no sistema."));

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new BusinessRuleException("Produto não encontrado no sistema."));

        UnitProduct unitProduct = new UnitProduct();
        unitProduct.setUnit(unit);
        unitProduct.setProduct(product);

        // Garante a consistência do catálogo: produtos criados sem estoque nascem indisponíveis.
        if (unitProduct.getQuantity() == null || unitProduct.getQuantity() == 0) {
            unitProduct.setIsAvailable(false);
        } else {
            unitProduct.setIsAvailable(request.isAvailable());
        }

        UnitProduct saved = unitProductRepository.save(unitProduct);

        return new UnitProductResponseDTO(
                saved.getId(),
                saved.getUnit().getId(),
                saved.getProduct().getId(),
                saved.getIsAvailable(),
                saved.getQuantity() // <- Parâmetro adicionado para bater com o novo DTO
        );
    }

    @Transactional(readOnly = true)
    public List<UnitProductResponseDTO> findAll() {
        return unitProductRepository.findAll().stream()
                .map(up -> new UnitProductResponseDTO(
                        up.getId(),
                        up.getUnit().getId(),
                        up.getProduct().getId(),
                        up.getIsAvailable(),
                        up.getQuantity()
                ))
                .toList();
    }

    @Transactional
    public UnitProductResponseDTO updateStock(Long unitId, Long productId, UnitProductUpdateDTO request) {

        UnitProduct unitProduct = unitProductRepository.findByUnitIdAndProductId(unitId, productId)
                .orElseThrow(() -> new BusinessRuleException("Vínculo entre filial e produto não encontrado."));

        unitProduct.setQuantity(request.quantity());

        // Regra de Negócio: Se a quantidade for 0, força a indisponibilidade.
        // Caso contrário, respeita o que o gestor enviou no JSON.
        if (request.quantity() == 0) {
            unitProduct.setIsAvailable(false);
        } else {
            unitProduct.setIsAvailable(request.isAvailable());
        }

        unitProduct = unitProductRepository.save(unitProduct);

        return new UnitProductResponseDTO(
                unitProduct.getId(),
                unitProduct.getUnit().getId(),
                unitProduct.getProduct().getId(),
                unitProduct.getIsAvailable(),
                unitProduct.getQuantity() // Certifique-se de que o seu ResponseDTO tem este campo mapeado!
        );
    }
}
