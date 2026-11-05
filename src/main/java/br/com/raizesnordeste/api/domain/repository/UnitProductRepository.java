package br.com.raizesnordeste.api.domain.repository;

import br.com.raizesnordeste.api.domain.entity.UnitProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositório para gestão do estoque distribuído e vínculo regional de produtos.
 */

@Repository
public interface UnitProductRepository extends JpaRepository<UnitProduct, Long> {

    /**
     * Valida a existência prévia de um vínculo para evitar duplicações
     * de um mesmo produto no catálogo de uma filial.
     */
    boolean existsByUnitIdAndProductId(Long unitId, Long productId);

    /**
     * Recupera o vínculo específico de um produto numa filial.
     * Fundamental para a dedução de estoque e validação de disponibilidade durante o checkout.
     */
    Optional<UnitProduct> findByUnitIdAndProductId(Long unitId, Long productId);
}
