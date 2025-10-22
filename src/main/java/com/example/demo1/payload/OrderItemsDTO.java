package com.example.demo1.payload;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Setter
public class OrderItemsDTO {
    private Integer orderItemId; 
    private Integer orderId;
    private ProductShortDTO product;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}
