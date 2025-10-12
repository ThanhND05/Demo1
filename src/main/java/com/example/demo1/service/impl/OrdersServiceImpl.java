package com.example.demo1.service.impl;

import com.example.demo1.converter.OrderItemsConverter;
import com.example.demo1.converter.OrdersConverter;
import com.example.demo1.entity.Cart;
import com.example.demo1.entity.CartItem;
import com.example.demo1.entity.OrderItems;
import com.example.demo1.entity.OrderStatus;
import com.example.demo1.entity.Orders;
import com.example.demo1.entity.User;
import com.example.demo1.payload.OrderItemsDTO;
import com.example.demo1.payload.OrdersDTO;
import com.example.demo1.payload.UserDTO;
import com.example.demo1.repository.CartRepository;
import com.example.demo1.repository.OrdersRepository;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.service.OrdersService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private OrdersConverter ordersConverter;
    @Autowired
    private OrderItemsConverter orderItemsConverter;
    
    @Override
    public List<OrdersDTO> findAll() {
        return ordersRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    private OrdersDTO toDTO(Orders savedOrder){
        //Tạo toDTO thủ công
        OrdersDTO dto = new OrdersDTO();
        dto.setOrderId(savedOrder.getOrderId());
        dto.setOrderDate(savedOrder.getOrderDate());
        dto.setStatus(savedOrder.getStatus());
        dto.setTotalAmount(savedOrder.getTotalAmount());
        dto.setShippingAddress(savedOrder.getShippingAddress());

        if (savedOrder.getUser() != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(savedOrder.getUser().getUserId());
            userDTO.setAddress(savedOrder.getUser().getAddress());
            userDTO.setName(savedOrder.getUser().getName());
            userDTO.setEmail(savedOrder.getUser().getEmail());
            userDTO.setPhone(savedOrder.getUser().getPhone());
            dto.setUser(userDTO);
        }
        Set<OrderItemsDTO> itemDTOs = new HashSet<>();
        if (savedOrder.getOrderItems() != null) {
            for (OrderItems item : savedOrder.getOrderItems()) {
                OrderItemsDTO itemDTO = orderItemsConverter.toDTO(item);
                itemDTOs.add(itemDTO);
            }
            dto.setOrderItems(itemDTOs);
        }
        return dto;
    }
    
    @Override
    public OrdersDTO createOrder(Integer userId, OrdersDTO orderDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        Cart cart = cartRepository.findByUser(user);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        Orders order = ordersConverter.toEntity(orderDTO);
        if (orderDTO.getShippingAddress() == null || orderDTO.getShippingAddress().trim().isEmpty()) {
            throw new RuntimeException("Shipping address is required");
        }
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        
        BigDecimal totalPrice = BigDecimal.ZERO;
        order.setTotalAmount(totalPrice);
        
        //Lấy hàng trong cart
        Set<CartItem> cartItems = cart.getItems();
        Set<OrderItems> orderItems = new HashSet<>();
        
        for (CartItem c : cartItems) {
            OrderItems item = new OrderItems();
            item.setProduct(c.getProduct());
            item.setPrice(c.getProduct().getPrice());
            item.setQuantity(c.getQuantity());
            item.setOrder(order);
            item.calculateSubTotal();
            orderItems.add(item);
        }
        //Xoá giỏ hàng đã đặt
        cart.getItems().clear();
        cartRepository.save(cart);
        order.setOrderItems(orderItems);
        order.calculateTotalAmount();
        Orders savedOrder = ordersRepository.save(order);
        return toDTO(savedOrder);
    }

    @Override
    public OrdersDTO getOrderById(Integer orderId) {
        Orders order = ordersRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found!"));
        return toDTO(order);
    }

    @Override
    public List<OrdersDTO> getOrdersByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) throw new RuntimeException("User not found!");
        List<Orders> ordersList = ordersRepository.findByUser_UserId(userId);
        return ordersList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public OrdersDTO updateOrder(Integer orderId, OrdersDTO orderDTO) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));
        if (order.getStatus() == OrderStatus.SHIPPING || order.getStatus() == OrderStatus.COMPLETED) {
            throw new RuntimeException("Cannot update order because it has already been shipped or completed!");
        }
        order.setShippingAddress(orderDTO.getShippingAddress());
        Orders updatedOrder = ordersRepository.save(order);
        return toDTO(updatedOrder);
    }
    @Override
    public OrdersDTO updateOrderStatus(Integer orderId, OrdersDTO orderDTO){
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));
        order.setStatus(orderDTO.getStatus());
        Orders updatedOrder = ordersRepository.save(order);
        return toDTO(updatedOrder);
    }
    @Override
    public OrdersDTO cancelOrder(Integer orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        if (order.getStatus() == OrderStatus.SHIPPING || order.getStatus() == OrderStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel order because it has already been shipped or completed!");
        }
        order.setStatus(OrderStatus.CANCELLED);
        Orders cancelledOrder = ordersRepository.save(order);
        return toDTO(cancelledOrder);
    }
    @Override
    public void deleteOrder(Integer orderId) {
        if (!ordersRepository.existsById(orderId))   throw new RuntimeException("Order not found!");
        ordersRepository.deleteById(orderId);
    }
}