package com.example.demo1.repository;

import com.example.demo1.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
    List<CartItem> findByCartCartId(Integer cartCartId);

    Optional<CartItem> findByCartCartIdAndProductProductId(Integer cartId, Integer productId);
}
