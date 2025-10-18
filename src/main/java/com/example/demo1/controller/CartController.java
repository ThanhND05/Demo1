package com.example.demo1.controller;

import com.example.demo1.payload.CartDTO;
import com.example.demo1.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Hiển thị giỏ hàng của người dùng theo userId
    @GetMapping("/user/{userId}")
    public String getCartByUserId(@PathVariable("userId") Integer userId, Model model) {
        CartDTO cartDTO = cartService.getCartByUserId(userId);
        model.addAttribute("cart", cartDTO);
        return "cart/view"; // => templates/cart/view.html
    }
}
