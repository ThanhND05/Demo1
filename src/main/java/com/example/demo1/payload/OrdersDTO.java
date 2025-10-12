package com.example.demo1.payload;

import com.example.demo1.entity.OrderItems;
import com.example.demo1.entity.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class OrdersDTO {
    private Integer orderId;
    private UserDTO user;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private Set<OrderItemsDTO> orderItems;
}
