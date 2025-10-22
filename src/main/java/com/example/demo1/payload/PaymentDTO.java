package com.example.demo1.payload;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Integer paymentId;
    private Integer orderId;
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private String paymentStatus;
    private String paymentMethod; // Kiểu String
    private String transactionId;
}