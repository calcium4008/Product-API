package com.example.product_api;

import java.math.BigDecimal;

public class ProductResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final int stock;
    private final String description;

    public ProductResponse(
            Long id,
            String name,
            BigDecimal price,
            int stock,
            String description
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getDescription() {
        return description;
    }
}