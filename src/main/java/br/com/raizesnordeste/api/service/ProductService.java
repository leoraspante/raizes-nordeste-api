package br.com.raizesnordeste.api.service;

import br.com.raizesnordeste.api.domain.entity.Product;
import br.com.raizesnordeste.api.domain.repository.ProductRepository;
import br.com.raizesnordeste.api.dto.request.ProductRequestDTO;
import br.com.raizesnordeste.api.dto.response.ProductResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponseDTO create(ProductRequestDTO request) {
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setCategory(request.category());
        product.setPrice(request.price());

        Product savedProduct = productRepository.save(product);

        return new ProductResponseDTO(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getCategory(),
                savedProduct.getPrice()
        );
    }

    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream()
                .map(product -> new ProductResponseDTO(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getCategory(),
                        product.getPrice()
                ))
                .collect(Collectors.toList());
    }
}
