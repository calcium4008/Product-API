package com.example.product_api;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(
            ProductRepository productRepository
    ) {
        this.productRepository = productRepository;
    }

    public Product getProduct(Long id) {

        Product product =
                productRepository.findById(id);

        if (product == null) {
            throw new ProductNotFoundException(id);
        }

        return product;
    }

    public Product createProduct(
            CreateProductRequest request
    ) {
        Product product = new Product(
                null,
                request.getName(),
                request.getPrice(),
                request.getStock(),
                request.getDescription()
        );

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(
            Long id,
            UpdateProductRequest request
    ) {
        Product product =
                productRepository.findById(id);

        if (product == null) {
            throw new ProductNotFoundException(id);
        }

        product.updateDetails(
                request.getName(),
                request.getPrice(),
                request.getStock(),
                request.getDescription()
        );

        return product;
    }

    public void deleteProduct(Long id) {

        Product product = getProduct(id);

        productRepository.delete(product);
    }

    public Page<Product> getProducts(
            int page,
            int size
    ) {
        Pageable pageable =
                PageRequest.of(page, size);

        return productRepository.findAll(pageable);
    }
}