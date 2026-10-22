package br.com.raizesnordeste.api.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Classe responsável pelo relacionamento entre filiais (Unit) e produtos (Product).
 * Permitindo o controle de estoque/disponibilidade de catálogo por unidade.
 */

@Entity
@Table(name = "tb_unit_products", uniqueConstraints = @UniqueConstraint(columnNames = {"unit_id", "product_id"}))
@Data
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
    private Boolean isAvailable = true;
}
