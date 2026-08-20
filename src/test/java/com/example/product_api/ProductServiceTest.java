package com.example.product_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository =
                mock(ProductRepository.class);

        productService =
                new ProductService(productRepository);
    }

    @Test
    void shouldReturnProductWhenProductExists() {

        Product mouse = new Product(
                1L,
                "Mouse",
                new BigDecimal("50.00"),
                10
        );

        when(productRepository.findById(1L))
                .thenReturn(mouse);

        Product result =
                productService.getProduct(1L);

        assertEquals("Mouse", result.getName());
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {

        when(productRepository.findById(999L))
                .thenReturn(null);

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProduct(999L)
        );
    }
}
