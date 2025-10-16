package com.example.demo1.controller;

import com.example.demo1.entity.OrderStatus;
import com.example.demo1.payload.OrdersDTO;
import com.example.demo1.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    // Tạo đơn hàng từ giỏ hàng của người dùng
    @PostMapping("/user/{userId}")
    public ResponseEntity<OrdersDTO> createOrderFromCart(@PathVariable Integer userId, @RequestBody Map<String, String> payload) {
        String shippingAddress = payload.get("shippingAddress");
        return ResponseEntity.ok(ordersService.createOrderFromCart(userId, shippingAddress));
    }

    // Lấy thông tin đơn hàng theo ID
    @GetMapping("/{orderId}")
    public ResponseEntity<OrdersDTO> getOrderById(@PathVariable Integer orderId) {
        return ResponseEntity.ok(ordersService.getOrderById(orderId));
    }

    // Lấy tất cả đơn hàng của một người dùng
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<OrdersDTO>> getOrdersByUserId(
            @PathVariable Integer userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(ordersService.getOrdersByUserId(userId, page, size));
    }

    // Cập nhật trạng thái đơn hàng (chỉ dành cho ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrdersDTO> updateOrderStatus(@PathVariable Integer orderId, @RequestBody Map<String, String> payload) {
        OrderStatus status = OrderStatus.valueOf(payload.get("status").toUpperCase());
        return ResponseEntity.ok(ordersService.updateOrderStatus(orderId, status));
    }
    @PutMapping("/user/{userId}/orders/{orderId}/cancel")
    public ResponseEntity<OrdersDTO> cancelOrder(
            @PathVariable Integer userId,
            @PathVariable Integer orderId) {

        OrdersDTO cancelledOrder = ordersService.cancelOrder(orderId, userId);
        return ResponseEntity.ok(cancelledOrder);
    }
    // Xóa đơn hàng
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Integer orderId) {
        ordersService.deleteOrder(orderId);
        return ResponseEntity.ok("Order deleted successfully!");
    }

    // --- API để quản lý các mục hàng (items) trong một đơn hàng ---

    // Thêm một sản phẩm vào đơn hàng đang chờ xử lý
    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrdersDTO> addItemToOrder(@PathVariable Integer orderId, @RequestBody Map<String, Integer> payload) {
        Integer productId = payload.get("productId");
        Integer quantity = payload.get("quantity");
        return ResponseEntity.ok(ordersService.addItemToOrder(orderId, productId, quantity));
    }

    // Cập nhật số lượng của một mục hàng trong đơn hàng
    @PutMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrdersDTO> updateOrderItemQuantity(
            @PathVariable Integer orderId,
            @PathVariable Integer itemId,
            @RequestBody Map<String, Integer> payload) {
        Integer newQuantity = payload.get("quantity");
        return ResponseEntity.ok(ordersService.updateOrderItemQuantity(orderId, itemId, newQuantity));
    }

    // Xóa một mục hàng khỏi đơn hàng
    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrdersDTO> deleteOrderItem(@PathVariable Integer orderId, @PathVariable Integer itemId) {
        return ResponseEntity.ok(ordersService.removeItemFromOrder(orderId, itemId));
    }
}