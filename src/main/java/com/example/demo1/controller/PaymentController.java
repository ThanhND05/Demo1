package com.example.demo1.controller;

import com.example.demo1.payload.PaymentResponse;
import com.example.demo1.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> pay(@PathVariable Integer orderId) {
        PaymentResponse response = paymentService.processPayment(orderId);
        return ResponseEntity.ok(response);
    }
}