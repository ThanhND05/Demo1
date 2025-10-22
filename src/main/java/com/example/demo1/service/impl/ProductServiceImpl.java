package com.example.demo1.service.impl;

import com.example.demo1.converter.ProductConverter;
import com.example.demo1.entity.Category;
import com.example.demo1.entity.Product;
import com.example.demo1.payload.ProductDTO;
import com.example.demo1.repository.CategoryRepository;
import com.example.demo1.repository.ProductRepository;
import com.example.demo1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductConverter productConverter;
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        Product entity = productConverter.toEntity(productDTO);
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Category ID: " + productDTO.getCategoryId()));
        entity.setCategory(category);
        entity.setImageUrl(productDTO.getImageUrl());
        entity.setSize(productDTO.getSize());
        entity.setColor(productDTO.getColor());
        entity.setStockQuantity(productDTO.getStockQuantity());
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
    public ProductDTO updateProduct(ProductDTO productDTO) {
        Product existing = productRepository.findById(productDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        existing.setProductName(productDTO.getProductName());
        existing.setDescription(productDTO.getDescription());
        existing.setPrice(productDTO.getPrice());
        existing.setStockQuantity(productDTO.getStockQuantity());
        existing.setSize(productDTO.getSize());
        existing.setColor(productDTO.getColor());

        if (productDTO.getImageUrl() != null && !productDTO.getImageUrl().isEmpty()) {
            existing.setImageUrl(productDTO.getImageUrl());
        }

        if (productDTO.getCategoryId() != null &&
                !productDTO.getCategoryId().equals(existing.getCategory().getCategoryId())) {

            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Category ID: " + productDTO.getCategoryId()));
            existing.setCategory(category);
        }

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
