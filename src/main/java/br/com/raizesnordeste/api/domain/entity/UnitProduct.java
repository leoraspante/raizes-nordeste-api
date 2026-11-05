package br.com.raizesnordeste.api.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Entidade responsável pelo controle de estoque distribuído.
 * Vincula um produto global do catálogo a uma filial específica,
 * permitindo gerir a quantidade física disponível e o status de ativação regional.
 */

@Entity
@Table(name = "tb_unit_products", uniqueConstraints = @UniqueConstraint(columnNames = {"unit_id", "product_id"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnitProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id",  nullable = false)
    private Unit unit;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",  nullable = false)
    private Product product;

    @Column(name = "is_available", nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;

    @Column(nullable = false)
    @Builder.Default
    private Integer quantity = 0;
}
