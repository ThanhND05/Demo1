package com.example.demo1.service.impl;

import com.example.demo1.converter.CategoryConverter;
import com.example.demo1.entity.Category;
import com.example.demo1.payload.CategoryDTO;
import com.example.demo1.repository.CategoryRepository;
import com.example.demo1.service.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryConverter categoryConverter;

    @Override
    public CategoryDTO addCategory(CategoryDTO category) {
        Category categoryEntity = categoryConverter.toEntity(category);
        Category saved = categoryRepository.save(categoryEntity);
        return categoryConverter.toDTO(saved);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> entities =  categoryRepository.findAll();
        List<CategoryDTO> dto= new ArrayList<>();
        for(Category categoryEntity : entities){
            dto.add(categoryConverter.toDTO(categoryEntity));
        }
        return dto;
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO category) {
        Category existing = categoryRepository.findById(category.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        existing.setCategoryName(category.getCategoryName());
        existing.setDescription(category.getDescription());
        Category updated = categoryRepository.save(existing);
        return categoryConverter.toDTO(updated);
    }

    @Override
    public CategoryDTO getCategoryById(Integer categoryId){
        Category entity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryConverter.toDTO(entity);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Category existing = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(existing);
    }

    @Override
    public List<CategoryDTO> searchCategoryByName(String categoryName) {
        List<Category> entities = categoryRepository.findByCategoryNameContainingIgnoreCase(categoryName);
        return entities.stream().map(categoryConverter::toDTO).collect(Collectors.toList());
    }

}
