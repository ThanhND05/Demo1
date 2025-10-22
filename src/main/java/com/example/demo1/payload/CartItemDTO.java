package com.example.demo1.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {
    private Integer cartItemId;
    private Integer quantity;
    private Integer cartId;
    private ProductShortDTO product;

}
