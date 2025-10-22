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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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
        Cart cart = cartRepository.findById(cartItemDTO.getCartId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Integer productId = cartItemDTO.getProduct().getProductId();
        Integer requestedQuantity = cartItemDTO.getQuantity();

        if (productId == null || requestedQuantity == null || requestedQuantity <= 0) {
            throw new IllegalArgumentException("Thông tin sản phẩm hoặc số lượng không hợp lệ.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        if (product.getStockQuantity() < requestedQuantity) {
            throw new RuntimeException("Sản phẩm '" + product.getProductName() + "' không đủ số lượng tồn kho. Chỉ còn lại: " + product.getStockQuantity());
        }

        CartItem existingItem = cartItemRepository.findByCartCartIdAndProductProductId(cart.getCartId(), productId)
                .orElse(null);

        CartItem cartItem;
        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + requestedQuantity;
            if (product.getStockQuantity() < newQuantity) {
                throw new RuntimeException("Tổng số lượng sản phẩm '" + product.getProductName() + "' trong giỏ (" + newQuantity + ") vượt quá số lượng tồn kho (" + product.getStockQuantity() + ").");
            }
            existingItem.setQuantity(newQuantity);
            cartItem = cartItemRepository.save(existingItem);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(requestedQuantity);
            cartItem = cartItemRepository.save(cartItem);
        }
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
