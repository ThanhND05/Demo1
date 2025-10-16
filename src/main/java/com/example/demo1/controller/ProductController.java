package com.example.demo1.controller;

import com.example.demo1.payload.ProductDTO;
import com.example.demo1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


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
    public Page<ProductDTO> getAllProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return productService.getAllProducts(page, size);
    }

    @PutMapping
    public ProductDTO updateProduct(@RequestBody ProductDTO productDTO) {
        return productService.updateProduct(productDTO);
    }

    @GetMapping("/id/{productId}")
    public ProductDTO getProductById(@PathVariable("productId") Integer productId) {
        return productService.getProductById(productId);
    }

    @GetMapping("/name/{productName}")
    public Page<ProductDTO> getProductByName(
            @PathVariable("productName") String productName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return productService.searchProductsByName(productName, page, size);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable("productId") Integer productId) {
        productService.deleteProduct(productId);
    }

    @GetMapping("/categories/name/{categoryName}")
    public Page<ProductDTO> getProductsByCategoryName(
            @PathVariable("categoryName") String categoryName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return productService.getProductsByCategoryName(categoryName, page, size);
    }

    @GetMapping("/categories/id/{categoryId}")
    public Page<ProductDTO> getProductsByCategoryId(
            @PathVariable("categoryId") Integer categoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return productService.getProductsByCategoryId(categoryId, page, size);
    }
}
