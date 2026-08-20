package com.example.product_api;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    private int stock;

    private String description;

    protected Product() {
    }

    public Product(
            Long id,
            String name,
            BigDecimal price,
            int stock
    ) {
        this(id, name, price, stock, null);
    }

    public Product(
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

    public void updateDetails(
            String name,
            BigDecimal price,
            int stock,
            String description
    ) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }
}