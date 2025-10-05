package com.example.demo1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;
    @Column(name="name")
    private String productName;
    private String description;
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    private Integer stockQuantity;
    @Enumerated(EnumType.STRING)
    private ProductEnum size;
    private String color;
    private String imageUrl;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name="categoryId", nullable = false)
    private Category category;
}
