package com.example.demo1.controller;

import com.example.demo1.entity.User;
import com.example.demo1.payload.CartDTO;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/my-cart")
    public String getMyCart(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/auth/login";
        }

        String username = authentication.getName();
        User currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user: " + username));
        CartDTO cartDTO = cartService.getCartByUserId(currentUser.getUserId());
        model.addAttribute("cart", cartDTO);

        return "cart/view";
    }

}
