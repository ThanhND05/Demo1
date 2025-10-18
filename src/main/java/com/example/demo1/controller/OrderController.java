package com.example.demo1.controller;

import com.example.demo1.entity.OrderStatus;
import com.example.demo1.payload.OrdersDTO;
import com.example.demo1.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    // ----- HIỂN THỊ DANH SÁCH ĐƠN HÀNG CỦA NGƯỜI DÙNG -----
    @GetMapping("/user/{userId}")
    public String getOrdersByUserId(
            @PathVariable Integer userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {

        Page<OrdersDTO> ordersPage = ordersService.getOrdersByUserId(userId, page, size);
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("userId", userId);
        return "orders/list"; // templates/orders/list.html
    }

    // ----- HIỂN THỊ CHI TIẾT ĐƠN HÀNG -----
    @GetMapping("/{orderId}")
    public String getOrderById(@PathVariable Integer orderId, Model model) {
        OrdersDTO order = ordersService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "orders/detail"; // templates/orders/detail.html
    }

    // ----- TẠO ĐƠN HÀNG TỪ GIỎ HÀNG -----
    @GetMapping("/user/{userId}/create")
    public String showCreateOrderForm(@PathVariable Integer userId, Model model) {
        model.addAttribute("userId", userId);
        return "orders/create"; // templates/orders/create.html
    }

    @PostMapping("/user/{userId}")
    public String createOrderFromCart(@PathVariable Integer userId, @RequestParam String shippingAddress) {
        ordersService.createOrderFromCart(userId, shippingAddress);
        return "redirect:/orders/user/" + userId;
    }

    // ----- CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG -----
    @PostMapping("/{orderId}/status")
    public String updateOrderStatus(@PathVariable Integer orderId, @RequestParam String status) {
        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
        ordersService.updateOrderStatus(orderId, newStatus);
        return "redirect:/orders/" + orderId;
    }

    // ----- HỦY ĐƠN HÀNG (USER) -----
    @PostMapping("/user/{userId}/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable Integer userId, @PathVariable Integer orderId) {
        ordersService.cancelOrder(orderId, userId);
        return "redirect:/orders/user/" + userId;
    }

    // ----- XOÁ ĐƠN HÀNG (ADMIN) -----
    @GetMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable Integer orderId) {
        ordersService.deleteOrder(orderId);
        return "redirect:/orders";
    }

    // ----- THÊM SẢN PHẨM VÀO ĐƠN HÀNG -----
    @PostMapping("/{orderId}/items")
    public String addItemToOrder(@PathVariable Integer orderId,
                                 @RequestParam Integer productId,
                                 @RequestParam Integer quantity) {
        ordersService.addItemToOrder(orderId, productId, quantity);
        return "redirect:/orders/" + orderId;
    }

    // ----- CẬP NHẬT SỐ LƯỢNG MỤC HÀNG -----
    @PostMapping("/{orderId}/items/{itemId}/update")
    public String updateOrderItemQuantity(@PathVariable Integer orderId,
                                          @PathVariable Integer itemId,
                                          @RequestParam Integer quantity) {
        ordersService.updateOrderItemQuantity(orderId, itemId, quantity);
        return "redirect:/orders/" + orderId;
    }

    // ----- XOÁ MỘT MỤC HÀNG -----
    @GetMapping("/{orderId}/items/{itemId}/delete")
    public String deleteOrderItem(@PathVariable Integer orderId, @PathVariable Integer itemId) {
        ordersService.removeItemFromOrder(orderId, itemId);
        return "redirect:/orders/" + orderId;
    }
}
