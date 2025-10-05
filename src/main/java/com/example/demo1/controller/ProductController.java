package com.example.demo1.controller;

import com.example.demo1.payload.ProductDTO;
import com.example.demo1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        return productService.addProduct(productDTO);
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping
    public ProductDTO updateProduct(@RequestBody ProductDTO productDTO) {
        return productService.updateProduct(productDTO);
    }

    @GetMapping("/{productId}")
    public ProductDTO getProductById(@PathVariable("productId") Integer productId) {
        return productService.getProductById(productId);
    }

    @GetMapping("/{productName}")
    public List<ProductDTO> getProductByName(@PathVariable("productName") String productName) {
        return productService.searchProductsByName(productName);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable("productId") Integer productId) {
        productService.deleteProduct(productId);
    }

    @GetMapping("/categories/name/{categoryName}")
    public List<ProductDTO> getProductsByCategoryName(@PathVariable("categoryName") String categoryName) {
        return productService.getProductsByCategoryName(categoryName);
    }

    @GetMapping("/categories/id/{categoryId}")
    public List<ProductDTO> getProductsByCategoryId(@PathVariable("categoryId") Integer categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }
}
