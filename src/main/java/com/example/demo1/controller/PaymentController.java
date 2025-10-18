package com.example.demo1.controller;

import com.example.demo1.payload.PaymentResponse;
import com.example.demo1.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // ----- HIỂN THỊ TRANG THANH TOÁN -----
    @GetMapping("/order/{orderId}")
    public String showPaymentPage(@PathVariable Integer orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "payment/payment"; // templates/payment/payment.html
    }

    // ----- XỬ LÝ THANH TOÁN -----
    @PostMapping("/order/{orderId}")
    public String processPayment(@PathVariable Integer orderId, Model model) {
        PaymentResponse response = paymentService.processPayment(orderId);
        model.addAttribute("paymentResponse", response);

        // Giả sử paymentResponse có thuộc tính "success" để check kết quả
        if (response.isSuccess()) {
            return "payment/success"; // templates/payment/success.html
        } else {
            return "payment/failure"; // templates/payment/failure.html
        }
    }
}
