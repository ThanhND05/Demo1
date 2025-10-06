package com.example.demo1.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    private Integer categoryId;
    private String categoryName;
    private String description;
}
