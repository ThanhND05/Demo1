package com.example.demo1.service.impl;

import com.example.demo1.entity.*;
import com.example.demo1.payload.PaymentDTO;
import com.example.demo1.payload.PaymentResponse;
import com.example.demo1.repository.OrdersRepository;
import com.example.demo1.repository.PaymentRepository;
import com.example.demo1.repository.ProductRepository;
import com.example.demo1.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired private OrdersRepository ordersRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private ProductRepository productRepository;
    @Override
    @Transactional
    public PaymentResponse processPayment(Integer orderId, String paymentMethod) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));
        if (order.getStatus() == OrderStatus.COMPLETED) {
            return new PaymentResponse(null, "Đơn hàng đã được thanh toán!", false);
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            return new PaymentResponse(null, "Không thể thanh toán đơn hàng đã hủy!", false);
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentDate(LocalDateTime.now());
        String methodUpper = paymentMethod.toUpperCase();
        payment.setPaymentMethod(methodUpper);

        boolean paymentSuccess = false;
        String message = "";

        switch (methodUpper) {
            case "COD":
            case "VNPAY":
            case "MOMO":
                Set<OrderItems> items = order.getOrderItems();
                try {
                    for (OrderItems item : items) {
                        Product product = productRepository.findById(item.getProduct().getProductId())
                                .orElseThrow(() -> new RuntimeException("Sản phẩm ID " + item.getProduct().getProductId() + " không tồn tại!"));

                        int requestedQuantity = item.getQuantity();
                        int currentStock = product.getStockQuantity();

                        if (currentStock < requestedQuantity) {
                            throw new RuntimeException("Không đủ số lượng tồn kho cho sản phẩm: " + product.getProductName() + ". Còn lại: " + currentStock);
                        }
                        product.setStockQuantity(currentStock - requestedQuantity);
                        productRepository.save(product);
                    }
                } catch (RuntimeException e) {
                    return new PaymentResponse(null, "Lỗi xử lý đơn hàng: " + e.getMessage(), false);
                }

                if (methodUpper.equals("COD")) {
                    payment.setPaymentStatus("PENDING");
                    order.setStatus(OrderStatus.SHIPPING);
                    message = "Đặt hàng COD thành công! Đơn hàng đang được xử lý.";
                } else {
                    payment.setPaymentStatus("SUCCESS");
                    payment.setTransactionId("SIMULATED_" + System.currentTimeMillis());
                    order.setStatus(OrderStatus.SHIPPING);
                    message = "Thanh toán qua " + paymentMethod + " thành công!";
                }
                paymentRepository.save(payment);
                ordersRepository.save(order);
                paymentSuccess = true;
                break;

            default:
                message = "Phương thức thanh toán không hợp lệ!";
                paymentSuccess = false;
                break;
        }

        PaymentDTO paymentDTO = null;
        if (payment.getPaymentId() != null) {
            paymentDTO = new PaymentDTO();
            paymentDTO.setPaymentId(payment.getPaymentId());
            paymentDTO.setOrderId(order.getOrderId());
            paymentDTO.setAmount(payment.getAmount());
            paymentDTO.setPaymentDate(payment.getPaymentDate());
            paymentDTO.setPaymentStatus(payment.getPaymentStatus());
            paymentDTO.setPaymentMethod(payment.getPaymentMethod());
            paymentDTO.setTransactionId(payment.getTransactionId());
        }
        return new PaymentResponse(paymentDTO, message, paymentSuccess);
    }
}