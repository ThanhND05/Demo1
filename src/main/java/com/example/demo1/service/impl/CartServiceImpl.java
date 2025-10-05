package com.example.demo1.service.impl;

import com.example.demo1.converter.CartConverter;
import com.example.demo1.entity.Cart;
import com.example.demo1.entity.User;
import com.example.demo1.payload.CartDTO;
import com.example.demo1.repository.CartRepository;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartConverter cartConverter;

    @Override
    public CartDTO getCartByUserId(Integer userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new RuntimeException("User not found!"));
        Cart cart = user.getCart();
        if(cart==null){
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }
        return cartConverter.toDTO(cart);
    }

}
