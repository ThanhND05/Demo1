package com.example.demo1.converter;

import com.example.demo1.entity.Product;
import com.example.demo1.payload.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductConverter {
    @Mapping(source = "category.categoryId", target = "categoryId")
    @Mapping(source = "category.categoryName", target = "categoryName")
    ProductDTO toDTO(Product product);

    @Mapping(source = "categoryId", target = "category.categoryId")
    Product toEntity(ProductDTO productDTO);
}
