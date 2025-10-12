package com.example.demo1.service.impl;

import com.example.demo1.entity.Orders;
import com.example.demo1.entity.OrderStatus;
import com.example.demo1.entity.Payment;
import com.example.demo1.payload.PaymentDTO;
import com.example.demo1.payload.PaymentResponse;
import com.example.demo1.repository.OrdersRepository;
import com.example.demo1.repository.PaymentRepository;
import com.example.demo1.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    @Transactional
    public PaymentResponse processPayment(Integer orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new RuntimeException("Order has been paid already!");
        }
        
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot process payment for a cancelled order!");
        }

        // Simulate a successful payment
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        // Update order status
        order.setStatus(OrderStatus.COMPLETED);
        ordersRepository.save(order);

        // Create DTO for response
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setPaymentId(payment.getPaymentId());
        paymentDTO.setOrderId(order.getOrderId());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setPaymentDate(payment.getPaymentDate());
        paymentDTO.setPaymentStatus(payment.getPaymentStatus());
        
        return new PaymentResponse(paymentDTO, "Payment successful!", true);
    }
}