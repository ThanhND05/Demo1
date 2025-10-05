package com.example.demo1.controller;

import com.example.demo1.payload.CartItemDTO;
import com.example.demo1.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cartitems")
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItemDTO>> getItemsByCartId(@PathVariable("cartId") Integer cartId) {
        List<CartItemDTO> items = cartItemService.getCartItemsByCartId(cartId);
        return ResponseEntity.ok(items);
    }
    @PostMapping
    public ResponseEntity<CartItemDTO> addCartItem(@RequestBody CartItemDTO cartItemDTO) {
        CartItemDTO savedItem = cartItemService.addCartItem(cartItemDTO);
        return ResponseEntity.ok(savedItem);
    }
    @PutMapping
    public ResponseEntity<CartItemDTO> updateCartItem(@RequestBody CartItemDTO cartItemDTO) {
        CartItemDTO updatedItem = cartItemService.updateCartItem(cartItemDTO);
        return ResponseEntity.ok(updatedItem);
    }
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable("cartItemId") Integer cartItemId) {
        cartItemService.deleteCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }
}
