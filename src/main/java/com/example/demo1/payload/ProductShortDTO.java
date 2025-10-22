package com.example.demo1.payload;

import com.example.demo1.entity.ProductEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductShortDTO {
    private Integer productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private ProductEnum size;
    private String color;
    private String imageUrl;
}
