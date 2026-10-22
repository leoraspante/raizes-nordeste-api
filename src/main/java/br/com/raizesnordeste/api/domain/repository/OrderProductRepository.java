package br.com.raizesnordeste.api.domain.repository;

import br.com.raizesnordeste.api.domain.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
