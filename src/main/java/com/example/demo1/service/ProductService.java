package com.example.demo1.service;

import com.example.demo1.payload.CategoryDTO;
import com.example.demo1.payload.ProductDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Page<ProductDTO> getAllProducts(int page, int size);
    Page<ProductDTO> searchProductsByName(String productName, int page, int size);
    Page<ProductDTO> getProductsByCategoryName(String categoryName, int page, int size);
    Page<ProductDTO> getProductsByCategoryId(Integer categoryId, int page, int size);


    ProductDTO addProduct(ProductDTO product);
    ProductDTO updateProduct(ProductDTO product);
    ProductDTO getProductById(Integer productId);
    void deleteProduct(Integer productId);
}
