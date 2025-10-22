package com.example.demo1.service;

import com.example.demo1.entity.OrderStatus;
import com.example.demo1.payload.OrdersDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrdersService {
    OrdersDTO createOrderFromCart(Integer userId, String shippingAddress);
    OrdersDTO getOrderById(Integer orderId);
    Page<OrdersDTO> getOrdersByUserId(Integer userId, int page, int size);
    OrdersDTO updateOrderStatus(Integer orderId, OrderStatus status);
    void deleteOrder(Integer orderId);
    OrdersDTO cancelOrder(Integer orderId, Integer userId);
    OrdersDTO addItemToOrder(Integer orderId, Integer productId, int quantity);
    OrdersDTO updateOrderItemQuantity(Integer orderId, Integer orderItemId, int newQuantity);
    OrdersDTO removeItemFromOrder(Integer orderId, Integer orderItemId);

}
