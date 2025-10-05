package com.example.demo1.service;

import com.example.demo1.payload.CartItemDTO;

import java.util.List;

public interface CartItemService {
    CartItemDTO addCartItem(CartItemDTO cartItemDTO);
    CartItemDTO updateCartItem(CartItemDTO cartItemDTO);
    void deleteCartItem(Integer cartItemId);
    List<CartItemDTO> getCartItemsByCartId(Integer cartId);
}
