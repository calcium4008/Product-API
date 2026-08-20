package com.example.product_api;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public class JpaProductRepository
        implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    public JpaProductRepository(
            ProductJpaRepository productJpaRepository
    ) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Product findById(Long id) {
        return productJpaRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public void delete(Product product) {
        productJpaRepository.delete(product);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productJpaRepository.findAll(pageable);
    }
}
