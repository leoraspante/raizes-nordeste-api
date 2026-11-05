package br.com.raizesnordeste.api.domain.repository;

import br.com.raizesnordeste.api.domain.entity.UnitProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UnitProductRepository extends JpaRepository<UnitProduct, Long> {

    boolean existsByUnitIdAndProductId(Long unitId, Long productId);
    Optional<UnitProduct> findByUnitIdAndProductId(Long unitId, Long productId);
}
