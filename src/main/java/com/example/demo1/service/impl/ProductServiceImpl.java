package com.example.demo1.service.impl;

import com.example.demo1.converter.ProductConverter;
import com.example.demo1.entity.Category;
import com.example.demo1.entity.Product;
import com.example.demo1.payload.ProductDTO;
import com.example.demo1.repository.ProductRepository;
import com.example.demo1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<ProductDTO> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productConverter::toDTO);
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
    public Page<ProductDTO> searchProductsByName(String productName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findByProductNameContainingIgnoreCase(productName, pageable);
        return productPage.map(productConverter::toDTO);
    }

    @Override
    public Page<ProductDTO> getProductsByCategoryName(String categoryName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findByCategory_CategoryNameIgnoreCase(categoryName, pageable);
        return productPage.map(productConverter::toDTO);
    }

    @Override
    public Page<ProductDTO> getProductsByCategoryId(Integer categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findByCategory_CategoryId(categoryId, pageable);
        return productPage.map(productConverter::toDTO);
    }
}
