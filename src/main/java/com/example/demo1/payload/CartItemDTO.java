package com.example.demo1.payload;

import lombok.Data;

@Data
public class CartItemDTO {
    private Integer cartItemId;
    private Integer productId;
    private Integer quantity;
    private Integer cartId;
}
