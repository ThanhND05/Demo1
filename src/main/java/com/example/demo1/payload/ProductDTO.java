package com.example.demo1.payload;

import com.example.demo1.entity.ProductEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductDTO {
    private Integer productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private ProductEnum size;
    private String color;
    private String imageUrl;
    private LocalDateTime createdAt = LocalDateTime.now();

    private Integer categoryId;

    private String categoryName;
}
