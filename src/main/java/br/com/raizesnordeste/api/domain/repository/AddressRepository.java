package br.com.raizesnordeste.api.domain.repository;

import br.com.raizesnordeste.api.domain.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Camada de acesso a dados para os endereços de entrega.
 */

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Recupera a carteira de endereços cadastrados de um cliente específico.
     * Utilizado para listagem no momento do checkout.
     */
    List<Address> findByUserId(Long userId);
}
