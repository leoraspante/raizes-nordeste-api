package br.com.raizesnordeste.api.domain.repository;

import br.com.raizesnordeste.api.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface responsável pela persistência do motor de vendas e histórico de pedidos.
 */

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
