package com.example.demo1.repository;

import com.example.demo1.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByProductNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByCategory_CategoryNameIgnoreCase(String categoryName, Pageable pageable);
    Page<Product> findByCategory_CategoryId(Integer categoryId, Pageable pageable);
}
