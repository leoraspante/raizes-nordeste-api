package br.com.raizesnordeste.api.domain.repository;

import br.com.raizesnordeste.api.domain.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório de acesso aos dados geográficos e cadastrais das filiais físicas.
 */

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    /**
     * Garante a unicidade no cadastro de novas filiais, bloqueando nomes duplicados.
     */
    boolean existsByName(String name);
}
