package com.example.demo1.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private PaymentDTO data;
    private String message;
    private boolean success;
}