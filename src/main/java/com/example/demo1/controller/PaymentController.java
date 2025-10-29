package com.example.demo1.controller;

import com.example.demo1.entity.User;
import com.example.demo1.payload.OrdersDTO;
import com.example.demo1.payload.PaymentResponse;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.service.OrdersService;
import com.example.demo1.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired private PaymentService paymentService;
    @Autowired private OrdersService ordersService;
    @Autowired private UserRepository userRepository;

    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Người dùng chưa đăng nhập.");
        }
        String identifier = authentication.getName();
        return userRepository.findByEmail(identifier)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user: " + identifier));
    }

    @GetMapping("/order/{orderId}")
    public String showPaymentPage(@PathVariable Integer orderId, Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);
        OrdersDTO order = ordersService.getOrderById(orderId);
        if (order.getStatus() != com.example.demo1.entity.OrderStatus.PENDING) {
            model.addAttribute("error", "Đơn hàng này không thể thanh toán (trạng thái: " + order.getStatus() + ").");
            return "orders/detail";
        }

        model.addAttribute("orderId", orderId);
        return "payment/payment";
    }

    @PostMapping("/order/{orderId}")
    public String processPayment(@PathVariable Integer orderId,
                                 @RequestParam String method,
                                 Authentication authentication,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        User currentUser = getCurrentUser(authentication);
        OrdersDTO order = ordersService.getOrderById(orderId);

        if (!order.getUserId().equals(currentUser.getUserId())) {
            redirectAttributes.addFlashAttribute("error", "Lỗi bảo mật: Không đúng đơn hàng.");
            return "redirect:/orders/my-orders";
        }
        if (order.getStatus() != com.example.demo1.entity.OrderStatus.PENDING) {
            redirectAttributes.addFlashAttribute("error", "Đơn hàng không còn ở trạng thái chờ thanh toán.");
            return "redirect:/orders/" + orderId; // Quay lại chi tiết đơn hàng
        }

        PaymentResponse response = paymentService.processPayment(orderId, method);
        redirectAttributes.addFlashAttribute("paymentResponse", response);
        redirectAttributes.addFlashAttribute("orderId", orderId);
        if (response.isSuccess()) {
            return "redirect:/payments/success";
        } else {
            return "redirect:/payments/failure";
        }
    }

    @GetMapping("/success")
    public String showSuccessPage(@ModelAttribute("paymentResponse") PaymentResponse paymentResponse, Model model) {
        if (paymentResponse == null || !paymentResponse.isSuccess()) {
            return "redirect:/orders/my-orders";
        }
        model.addAttribute("paymentResponse", paymentResponse);
        return "payment/success";
    }

    @GetMapping("/failure")
    public String showFailurePage(@ModelAttribute("paymentResponse") PaymentResponse paymentResponse,
                                  @ModelAttribute("orderId") Integer orderId,
                                  Model model) {
        if (paymentResponse == null || paymentResponse.isSuccess()) {
            return "redirect:/orders/my-orders";
        }
        model.addAttribute("paymentResponse", paymentResponse);
        model.addAttribute("orderId", orderId);
        return "payment/failure";
    }

}
