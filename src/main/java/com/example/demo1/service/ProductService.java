package com.example.demo1.service;

import com.example.demo1.payload.CategoryDTO;
import com.example.demo1.payload.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO addProduct(ProductDTO product);
    List<ProductDTO> getAllProducts();
    ProductDTO updateProduct(ProductDTO product);
    ProductDTO getProductById(Integer productId);
    List<ProductDTO> searchProductsByName(String productName);
    void deleteProduct(Integer productId);
    List<ProductDTO> getProductsByCategoryName(String categoryName);
    List<ProductDTO> getProductsByCategoryId(Integer categoryId);
}
