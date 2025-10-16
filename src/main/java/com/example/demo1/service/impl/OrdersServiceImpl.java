package com.example.demo1.service.impl;

import com.example.demo1.converter.OrdersConverter;
import com.example.demo1.entity.*;
import com.example.demo1.exception.ResourceNotFoundException;
import com.example.demo1.payload.OrdersDTO;
import com.example.demo1.repository.*;
import com.example.demo1.service.OrdersService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class OrdersServiceImpl implements OrdersService {

    @Autowired private OrdersRepository ordersRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private OrdersConverter ordersConverter;

    @Override
    public OrdersDTO createOrderFromCart(Integer userId, String shippingAddress) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
        Cart cart = cartRepository.findByUser(user);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        Orders order = new Orders();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(shippingAddress);

        for (CartItem cartItem : cart.getItems()) {
            OrderItems orderItem = new OrderItems();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            order.addItem(orderItem);
        }

        // Entity sẽ tự tính tổng tiền nhờ @PrePersist/@PreUpdate
        Orders savedOrder = ordersRepository.save(order);

        // Xóa giỏ hàng sau khi tạo đơn hàng thành công
        cart.getItems().clear();
        cartRepository.save(cart);

        return ordersConverter.toDTO(savedOrder);
    }

    @Override
    public OrdersDTO getOrderById(Integer orderId) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found!"));
        return ordersConverter.toDTO(order);
    }
    @Override
    public Page<OrdersDTO> getOrdersByUserId(Integer userId, int page, int size) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Orders> ordersPage = ordersRepository.findByUser_UserId(userId, pageable);
        return ordersPage.map(ordersConverter::toDTO);
    }

    @Override
    public OrdersDTO updateOrderStatus(Integer orderId, OrderStatus status) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found!"));
        order.setStatus(status);
        // Không cần gọi save() tường minh nếu phương thức được bao bởi @Transactional
        return ordersConverter.toDTO(order);
    }
    @Override
    public OrdersDTO cancelOrder(Integer orderId, Integer userId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        // KIỂM TRA BẢO MẬT: Đảm bảo người dùng chỉ hủy đơn hàng của mình
        if (!order.getUser().getUserId().equals(userId)) {
            throw new SecurityException("User is not authorized to cancel this order.");
        }

        // KIỂM TRA LOGIC NGHIỆP VỤ: Chỉ hủy được đơn hàng đang chờ hoặc đang xử lý
        if (order.getStatus() == OrderStatus.SHIPPING || order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel an order that is already shipping or completed.");
        }

        // Cập nhật trạng thái
        order.setStatus(OrderStatus.CANCELLED);

        // @Transactional sẽ tự động lưu thay đổi khi kết thúc phương thức
        return ordersConverter.toDTO(order);
    }
    @Override
    public void deleteOrder(Integer orderId) {
        if (!ordersRepository.existsById(orderId)) {
            throw new RuntimeException("Order not found!");
        }
        ordersRepository.deleteById(orderId);
    }

    @Override
    public OrdersDTO addItemToOrder(Integer orderId, Integer productId, int quantity) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found!"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found!"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Cannot modify an order that is not in PENDING state.");
        }

        OrderItems newItem = new OrderItems();
        newItem.setProduct(product);
        newItem.setQuantity(quantity);
        newItem.setPrice(product.getPrice());

        order.addItem(newItem);
        Orders updatedOrder = ordersRepository.save(order);
        // JPA sẽ tự động lưu order và tính lại tổng tiền
        return ordersConverter.toDTO(updatedOrder);
    }

    @Override
    public OrdersDTO updateOrderItemQuantity(Integer orderId, Integer orderItemId, int newQuantity) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found!"));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Cannot modify an order that is not in PENDING state.");
        }

        OrderItems itemToUpdate = order.getOrderItems().stream()
                .filter(item -> item.getOrderItemId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in this order."));

        itemToUpdate.setQuantity(newQuantity);
        order.calculateTotalAmount();
        Orders updatedOrder = ordersRepository.save(order);
        // JPA sẽ tự động cập nhật item và tính lại tổng tiền của order
        return ordersConverter.toDTO(updatedOrder);
    }

    @Override
    public OrdersDTO removeItemFromOrder(Integer orderId, Integer orderItemId) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found!"));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Cannot modify an order that is not in PENDING state.");
        }

        OrderItems itemToRemove = order.getOrderItems().stream()
                .filter(item -> item.getOrderItemId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in this order."));

        Orders updatedOrder = ordersRepository.save(order);
        return ordersConverter.toDTO(updatedOrder);
    }
}