package com.example.demo1.controller;

import com.example.demo1.entity.OrderStatus;
import com.example.demo1.entity.User;
import com.example.demo1.payload.OrdersDTO;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")

public class OrderController {

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


    @GetMapping("/my-orders")
    public String getMyOrders(Authentication authentication,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size, Model model) {
        User currentUser = getCurrentUser(authentication);
        Page<OrdersDTO> ordersPage = ordersService.getOrdersByUserId(currentUser.getUserId(), page, size);
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("currentPage", page);
        return "orders/list";
    }

    @GetMapping("/{orderId}")
    public String getOrderById(@PathVariable Integer orderId, Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);
        OrdersDTO order = ordersService.getOrderById(orderId);

        if (!order.getUserId().equals(currentUser.getUserId())) {
            model.addAttribute("error", "Bạn không có quyền xem đơn hàng này.");
            return "error/403";
        }

        model.addAttribute("order", order);
        return "orders/detail";
    }

    @GetMapping("/create-from-cart")
    public String showCreateOrderForm(Authentication authentication) {
        getCurrentUser(authentication);
        return "orders/create";
    }

    @PostMapping("/create-from-cart")
    public String createOrderFromCart(Authentication authentication, @RequestParam String shippingAddress, RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication);
        try {
            ordersService.createOrderFromCart(currentUser.getUserId(), shippingAddress);
            redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công!");
            return "redirect:/orders/my-orders";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi tạo đơn hàng: " + e.getMessage());
            return "redirect:/cart/my-cart";
        }
    }

    @PostMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateOrderStatus(@PathVariable Integer orderId, @RequestParam String status, RedirectAttributes redirectAttributes) {
        try {
            OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
            ordersService.updateOrderStatus(orderId, newStatus);
            redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Trạng thái không hợp lệ!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi cập nhật trạng thái: "+ e.getMessage());
        }
        return "redirect:/admin/orders/" + orderId;
    }

    @PostMapping("/{orderId}/cancel")
    public String cancelOrder(Authentication authentication, @PathVariable Integer orderId, RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication);
        try {
            ordersService.cancelOrder(orderId, currentUser.getUserId());
            redirectAttributes.addFlashAttribute("success", "Hủy đơn hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể hủy đơn hàng: " + e.getMessage());
        }
        return "redirect:/orders/my-orders";
    }

    @PostMapping("/delete/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteOrder(@PathVariable Integer orderId, RedirectAttributes redirectAttributes) {
        try {
            ordersService.deleteOrder(orderId);
            redirectAttributes.addFlashAttribute("success", "Xóa đơn hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi xóa đơn hàng: " + e.getMessage());
        }
        return "redirect:/admin/orders";
    }

    @PostMapping("/{orderId}/items")
    @PreAuthorize("hasRole('ADMIN')")
    public String addItemToOrder(@PathVariable Integer orderId, @RequestParam Integer productId, @RequestParam Integer quantity, RedirectAttributes redirectAttributes) {
        try {
            ordersService.addItemToOrder(orderId, productId, quantity);
            redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi thêm sản phẩm: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + orderId;
    }

    @PostMapping("/{orderId}/items/{itemId}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateOrderItemQuantity(@PathVariable Integer orderId, @PathVariable Integer itemId, @RequestParam Integer quantity, RedirectAttributes redirectAttributes) {
        try {
            ordersService.updateOrderItemQuantity(orderId, itemId, quantity);
            redirectAttributes.addFlashAttribute("success", "Cập nhật số lượng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi cập nhật số lượng: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + orderId;
    }

    @PostMapping("/{orderId}/items/{itemId}/delete")
    public String deleteOrderItem(@PathVariable Integer orderId, @PathVariable Integer itemId, RedirectAttributes redirectAttributes, Authentication authentication) {
        try {
            ordersService.removeItemFromOrder(orderId, itemId);
            redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm khỏi đơn hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi xóa sản phẩm: " + e.getMessage());
        }
        return "redirect:/orders/" + orderId;
    }
}