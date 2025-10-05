package com.example.demo1.repository;

import com.example.demo1.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    List<Category> findByCategoryNameContainingIgnoreCase(String name);
}
