package com.example.demo1.repository;

import com.example.demo1.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
    List<CartItem> findByCartCartId(Integer cartCartId);
}
