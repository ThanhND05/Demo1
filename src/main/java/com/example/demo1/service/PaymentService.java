package com.example.demo1.service;

import com.example.demo1.payload.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(Integer orderId, String paymentMethod);
}