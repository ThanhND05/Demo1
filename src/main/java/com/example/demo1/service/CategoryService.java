package com.example.demo1.service;

import com.example.demo1.payload.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO addCategory(CategoryDTO category);
    List<CategoryDTO> getAllCategories();
    CategoryDTO updateCategory(CategoryDTO category);
    CategoryDTO getCategoryById(Integer categoryId);
    void deleteCategory(Integer categoryId);
    List<CategoryDTO> searchCategoryByName(String categoryName);
}
