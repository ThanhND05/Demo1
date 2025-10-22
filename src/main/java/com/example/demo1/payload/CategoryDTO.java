package com.example.demo1.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    private Integer categoryId;
    @NotEmpty(message = "Tên danh mục không được để trống") // <-- Thêm
    private String categoryName;
    private String description;
}
