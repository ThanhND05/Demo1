package com.example.demo1.converter;

import com.example.demo1.entity.Product;
import com.example.demo1.payload.ProductShortDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductShortConverter {
    ProductShortDTO toShortDTO(Product product);
}
