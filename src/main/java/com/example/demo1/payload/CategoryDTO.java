package com.example.demo1.payload;

import lombok.Data;

@Data
public class CategoryDTO {
    private Integer categoryId;
    private String categoryName;
    private String description;
}
