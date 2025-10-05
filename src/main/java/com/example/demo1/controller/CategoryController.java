package com.example.demo1.controller;

import com.example.demo1.payload.CategoryDTO;
import com.example.demo1.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public CategoryDTO createCategory(@RequestBody CategoryDTO category) {
        return categoryService.addCategory(category);
    }

    @GetMapping
    public List<CategoryDTO> getAllCategories()
    {
        return categoryService.getAllCategories();
    }

    @PutMapping
    public CategoryDTO updateCategory(@RequestBody CategoryDTO category) {
        return categoryService.updateCategory(category);
    }

    @GetMapping("/{categoryId}")
    public CategoryDTO getCategoryById(@PathVariable("categoryId") Integer categoryId) {
        return categoryService.getCategoryById(categoryId);
    }


    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable("categoryId") Integer categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    @GetMapping("/{categoryName}")
    public List<CategoryDTO> getCategoryByName(@PathVariable("categoryName") String categoryName) {
        return categoryService.searchCategoryByName(categoryName);
    }
}
