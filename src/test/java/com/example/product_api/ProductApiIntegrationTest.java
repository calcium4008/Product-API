package com.example.product_api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @BeforeEach
    void setUp() {
        productJpaRepository.deleteAll();
    }

    @Test
    void shouldReturnProductWhenProductExists() throws Exception {
        Product mouse = productJpaRepository.save(
                new Product(
                        null,
                        "Mouse",
                        new BigDecimal("50.00"),
                        10
                )
        );

        mockMvc.perform(
                        get("/products/" + mouse.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mouse.getId()))
                .andExpect(jsonPath("$.name").value("Mouse"))
                .andExpect(jsonPath("$.price").value(50.00))
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    void shouldReturn404WhenProductDoesNotExist() throws Exception {
        mockMvc.perform(
                        get("/products/999999")
                )
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.code")
                                .value("PRODUCT_NOT_FOUND")
                )
                .andExpect(
                        jsonPath("$.message")
                                .value("Product 999999 does not exist")
                );
    }

    @Test
    void shouldCreateProductWhenRequestIsValid() throws Exception {
        String requestBody = """
                {
                  "name": "Webcam",
                  "price": 150.00,
                  "stock": 10
                }
                """;

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Webcam"))
                .andExpect(jsonPath("$.price").value(150.00))
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    void shouldReturn400WhenPriceIsNegative() throws Exception {
        String requestBody = """
                {
                  "name": "Webcam",
                  "price": -100.00,
                  "stock": 10
                }
                """;

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.code")
                                .value("VALIDATION_ERROR")
                )
                .andExpect(
                        jsonPath("$.message")
                                .value("Request validation failed")
                );
    }

    @Test
    void shouldUpdateProductWhenProductExists() throws Exception {
        Product mouse = productJpaRepository.save(
                new Product(
                        null,
                        "Mouse",
                        new BigDecimal("50.00"),
                        10
                )
        );

        String requestBody = """
                {
                  "name": "Gaming Mouse",
                  "price": 80.00,
                  "stock": 15
                }
                """;

        mockMvc.perform(
                        put("/products/" + mouse.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mouse.getId()))
                .andExpect(jsonPath("$.name").value("Gaming Mouse"))
                .andExpect(jsonPath("$.price").value(80.00))
                .andExpect(jsonPath("$.stock").value(15));

        mockMvc.perform(
                        get("/products/" + mouse.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gaming Mouse"))
                .andExpect(jsonPath("$.price").value(80.00))
                .andExpect(jsonPath("$.stock").value(15));
    }

    @Test
    void shouldReturn404WhenUpdatingProductThatDoesNotExist()
            throws Exception {

        String requestBody = """
                {
                  "name": "Gaming Mouse",
                  "price": 80.00,
                  "stock": 15
                }
                """;

        mockMvc.perform(
                        put("/products/999999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.code")
                                .value("PRODUCT_NOT_FOUND")
                )
                .andExpect(
                        jsonPath("$.message")
                                .value("Product 999999 does not exist")
                );
    }

    @Test
    void shouldReturn400WhenUpdateRequestIsInvalid()
            throws Exception {

        Product mouse = productJpaRepository.save(
                new Product(
                        null,
                        "Mouse",
                        new BigDecimal("50.00"),
                        10
                )
        );

        String requestBody = """
                {
                  "name": "",
                  "price": -10.00,
                  "stock": -5
                }
                """;

        mockMvc.perform(
                        put("/products/" + mouse.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.code")
                                .value("VALIDATION_ERROR")
                )
                .andExpect(
                        jsonPath("$.message")
                                .value("Request validation failed")
                );;
    }

    @Test
    void shouldDeleteProductWhenProductExists() throws Exception {
        Product mouse = productJpaRepository.save(
                new Product(
                        null,
                        "Mouse",
                        new BigDecimal("50.00"),
                        10
                )
        );

        Long productId = mouse.getId();

        mockMvc.perform(
                        delete("/products/" + productId)
                )
                .andExpect(status().isNoContent());

        boolean exists = productJpaRepository
                .findById(productId)
                .isPresent();

        assertFalse(exists);
    }

    @Test
    void shouldReturn404WhenDeletingProductThatDoesNotExist()
            throws Exception {

        mockMvc.perform(
                        delete("/products/999999")
                )
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.code")
                                .value("PRODUCT_NOT_FOUND")
                )
                .andExpect(
                        jsonPath("$.message")
                                .value("Product 999999 does not exist")
                );
    }

    @Test
    void shouldReturnPaginatedProducts() throws Exception {

        productJpaRepository.save(
                new Product(
                        null,
                        "Mouse",
                        new BigDecimal("50.00"),
                        10
                )
        );

        productJpaRepository.save(
                new Product(
                        null,
                        "Keyboard",
                        new BigDecimal("100.00"),
                        5
                )
        );

        productJpaRepository.save(
                new Product(
                        null,
                        "Monitor",
                        new BigDecimal("600.00"),
                        3
                )
        );

        mockMvc.perform(
                        get("/products")
                                .param("page", "0")
                                .param("size", "2")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.pageable").doesNotExist())
                .andExpect(jsonPath("$.sort").doesNotExist());
    }

    @Test
    void shouldReturn400WhenPageIsNegative()
            throws Exception {

        mockMvc.perform(
                        get("/products")
                                .param("page", "-1")
                                .param("size", "20")
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.code")
                                .value("VALIDATION_ERROR")
                )
                .andExpect(
                        jsonPath("$.message")
                                .value("Request validation failed")
                );
    }

    @Test
    void shouldReturn400WhenPageSizeExceedsMaximum()
            throws Exception {

        mockMvc.perform(
                        get("/products")
                                .param("page", "0")
                                .param("size", "101")
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.code")
                                .value("VALIDATION_ERROR")
                )
                .andExpect(
                        jsonPath("$.message")
                                .value("Request validation failed")
                );
    }

    @Test
    void shouldCreateProductWithDescription()
            throws Exception {

        String requestBody = """
            {
              "name": "Mechanical Keyboard",
              "price": 250.00,
              "stock": 10,
              "description": "Wireless mechanical keyboard"
            }
            """;

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.name")
                                .value("Mechanical Keyboard")
                )
                .andExpect(
                        jsonPath("$.description")
                                .value("Wireless mechanical keyboard")
                );
    }

    @Test
    void shouldReturnProductDescription()
            throws Exception {

        Product product = productJpaRepository.save(
                new Product(
                        null,
                        "Mouse",
                        new BigDecimal("50.00"),
                        10,
                        "Wireless mouse"
                )
        );

        mockMvc.perform(
                        get("/products/" + product.getId())
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.description")
                                .value("Wireless mouse")
                );
    }

    @Test
    void shouldUpdateProductDescription()
            throws Exception {

        Product product = productJpaRepository.save(
                new Product(
                        null,
                        "Mouse",
                        new BigDecimal("50.00"),
                        10,
                        "Old description"
                )
        );

        String requestBody = """
            {
              "name": "Mouse",
              "price": 50.00,
              "stock": 10,
              "description": "Updated description"
            }
            """;

        mockMvc.perform(
                        put("/products/" + product.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.description")
                                .value("Updated description")
                );

        mockMvc.perform(
                        get("/products/" + product.getId())
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.description")
                                .value("Updated description")
                );
    }

    @Test
    void shouldReturn400WhenDescriptionExceedsMaximumLength()
            throws Exception {

        String longDescription = "a".repeat(501);

        String requestBody = """
            {
              "name": "Mouse",
              "price": 50.00,
              "stock": 10,
              "description": "%s"
            }
            """.formatted(longDescription);

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.code")
                                .value("VALIDATION_ERROR")
                )
                .andExpect(
                        jsonPath("$.message")
                                .value("Request validation failed")
                );
    }
}