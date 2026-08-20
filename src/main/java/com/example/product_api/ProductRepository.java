package com.example.product_api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {

    Product findById(Long id);

    Product save(Product product);

    void delete(Product product);

    Page<Product> findAll(Pageable pageable);

}