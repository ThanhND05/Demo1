package com.example.demo1.service.impl;

import com.example.demo1.converter.ProductConverter;
import com.example.demo1.entity.Category;
import com.example.demo1.entity.Product;
import com.example.demo1.payload.ProductDTO;
import com.example.demo1.repository.ProductRepository;
import com.example.demo1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductConverter productConverter;
    
    @Override
    public ProductDTO addProduct(ProductDTO product) {
        Product entity   = productConverter.toEntity(product);
        Category category = new Category();
        category.setCategoryId(product.getCategoryId());
        category.setCategoryName(product.getCategoryName());
        entity.setCategory(category);
        Product saved = productRepository.save(entity);
        return productConverter.toDTO(saved);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> entities =  productRepository.findAll();
        List<ProductDTO> dto= new ArrayList<>();
        for(Product productEntity : entities){
            dto.add(productConverter.toDTO(productEntity));
        }
        return dto;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO product) {
        Product existing = productRepository.findById(product.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        existing.setProductName(product.getProductName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStockQuantity(product.getStockQuantity());
        existing.setSize(product.getSize());
        existing.setColor(product.getColor());
        existing.setImageUrl(product.getImageUrl());
        Product updated = productRepository.save(existing);
        return productConverter.toDTO(updated);
    }

    @Override
    public ProductDTO getProductById(Integer productId){
        Product entity = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productConverter.toDTO(entity);
    }

    @Override
    public void deleteProduct(Integer productId) {
        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(existing);
    }
    @Override
    public List<ProductDTO> searchProductsByName(String productName) {
        List<Product> entities = productRepository.findByProductNameContainingIgnoreCase(productName);
        return entities.stream()
                .map(productConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsByCategoryName(String categoryName){
        List<Product> products = productRepository.findByCategory_CategoryNameIgnoreCase(categoryName);
        return products.stream()
                .map(productConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsByCategoryId(Integer categoryId){
        List<Product> products = productRepository.findByCategory_CategoryId(categoryId);
        return products.stream()
                .map(productConverter::toDTO)
                .collect(Collectors.toList());
    }
}
