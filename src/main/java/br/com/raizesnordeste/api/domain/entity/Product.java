package br.com.raizesnordeste.api.domain.entity;

import br.com.raizesnordeste.api.domain.enums.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * Entidade que representa o catálogo global de produtos da rede.
 * Define os metadados base (nome, categoria) e o preço sugerido/padrão,
 * que pode ser comercializado por diferentes filiais.
 */

@Entity
@Table(name = "tb_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}
