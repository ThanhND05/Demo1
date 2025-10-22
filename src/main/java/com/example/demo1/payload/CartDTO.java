package com.example.demo1.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class CartDTO {
    private Integer cartId;
    private LocalDateTime createdAt =  LocalDateTime.now();
    private UserDTO user;
    private Set<CartItemDTO> items;
}
