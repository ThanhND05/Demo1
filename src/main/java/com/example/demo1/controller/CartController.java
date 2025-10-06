package com.example.demo1.controller;

import com.example.demo1.payload.CartDTO;
import com.example.demo1.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable("userId") Integer userId) {
        CartDTO cartDTO = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartDTO);
    }

}
