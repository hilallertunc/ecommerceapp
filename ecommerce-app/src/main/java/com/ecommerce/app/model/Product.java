package com.ecommerce.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Ürün adı boş olamaz")
    @Column(nullable = false)
    private String name;
    
    @NotNull(message = "Fiyat boş olamaz")
    @Positive(message = "Fiyat pozitif olmalı")
    @Column(nullable = false)
    private Double price;
    
    @NotNull(message = "Stok miktarı boş olamaz")
    @Column(nullable = false)
    private Integer stock;
    
    private String description;

    // Constructors
    public Product() {}
    
    public Product(String name, Double price, Integer stock, String description) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}