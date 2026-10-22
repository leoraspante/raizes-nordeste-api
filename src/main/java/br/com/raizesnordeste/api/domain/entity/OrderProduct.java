package br.com.raizesnordeste.api.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * Classe responsável pela representação individual de cada item contido em um pedido.
 * Armazenando preço no momento da compra, garantindo o histórico financeiro.
 */

@Entity
@Table(name = "tb_order_products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @Positive // Garante que a quantidade mínima seja 1.
    @Column(nullable = false)
    private Integer quantity;

    // Garante o registro do preço cobrado por unidade no momento em que o pedido foi fechado.
    @NotNull
    @PositiveOrZero
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;
}
