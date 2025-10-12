package com.example.demo1.payload;

import com.example.demo1.entity.Orders;
import com.example.demo1.entity.Product;
import java.math.BigDecimal;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Setter
public class OrderItemsDTO {
    private Integer orderItemId; 
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}
