package com.example.demo1.converter;

import com.example.demo1.entity.Category;
import com.example.demo1.payload.CategoryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryConverter {
    CategoryDTO toDTO(Category category);
    Category toEntity(CategoryDTO categoryDTO);
}
