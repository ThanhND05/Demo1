package com.example.demo1.payload;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CartDTO {
    private Integer cartId;
    private LocalDateTime createdAt =  LocalDateTime.now();
    private Integer userId;
    private Set<CartItemDTO> items;
}
