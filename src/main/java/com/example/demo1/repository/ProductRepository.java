package com.example.demo1.repository;

import com.example.demo1.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByProductNameContainingIgnoreCase(String name);
    List<Product> findByCategory_CategoryNameIgnoreCase(String categoryName);
    List<Product> findByCategory_CategoryId(Integer categoryId);
}
