package com.example.demo1.service;

import com.example.demo1.entity.Cart;
import com.example.demo1.payload.CartDTO;

public interface CartService {
    CartDTO getCartByUserId(Integer userId);
}
