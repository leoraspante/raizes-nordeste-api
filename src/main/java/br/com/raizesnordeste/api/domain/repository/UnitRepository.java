package br.com.raizesnordeste.api.domain.repository;

import br.com.raizesnordeste.api.domain.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
}
