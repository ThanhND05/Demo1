package com.example.demo1.service.impl;

import com.example.demo1.converter.CartItemConverter;
import com.example.demo1.entity.Cart;
import com.example.demo1.entity.CartItem;
import com.example.demo1.entity.Product;
import com.example.demo1.payload.CartItemDTO;
import com.example.demo1.repository.CartItemRepository;
import com.example.demo1.repository.CartRepository;
import com.example.demo1.repository.ProductRepository;
import com.example.demo1.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemConverter cartItemConverter;

    @Override
    public CartItemDTO addCartItem(CartItemDTO cartItemDTO) {
        Cart cart = cartRepository.findById(cartItemDTO.getCartId()).
                orElseThrow(() -> new RuntimeException("Cart not found"));
        Product product = productRepository.findById(cartItemDTO.getProduct().getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemDTO.getQuantity());


        cartItem = cartItemRepository.save(cartItem);
        return cartItemConverter.toDTO(cartItem);
    }

    @Override
    public CartItemDTO updateCartItem(CartItemDTO cartItemDTO) {
        CartItem cartItem = cartItemRepository.findById(cartItemDTO.getCartItemId())
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        if(cartItemDTO.getQuantity() != null) {
            cartItem.setQuantity(cartItemDTO.getQuantity());
        }

        if(cartItemDTO.getProduct().getProductId() != null &&
                !cartItemDTO.getProduct().getProductId().equals(cartItem.getProduct().getProductId())) {

            Product product = productRepository.findById(cartItemDTO.getProduct().getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            cartItem.setProduct(product);
        }

        cartItem = cartItemRepository.save(cartItem);
        return cartItemConverter.toDTO(cartItem);
    }

    @Override
    public void deleteCartItem(Integer cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public List<CartItemDTO> getCartItemsByCartId(Integer cartId) {
        List<CartItem> items = cartItemRepository.findByCartCartId(cartId);
        return items.stream().map(cartItemConverter::toDTO).collect(Collectors.toList());
    }
}
