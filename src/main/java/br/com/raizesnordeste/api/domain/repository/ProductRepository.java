package br.com.raizesnordeste.api.domain.repository;

import br.com.raizesnordeste.api.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Camada de acesso aos dados do catálogo global de produtos da rede.
 */

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
