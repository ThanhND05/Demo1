package com.example.demo1.repository;

import com.example.demo1.entity.Cart;
import com.example.demo1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Integer> {
   public Cart findByUser(User user);
}
