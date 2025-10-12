package com.example.demo1.service;

import com.example.demo1.payload.OrdersDTO; 
import java.util.List;

public interface OrdersService {
    List<OrdersDTO> findAll();
    OrdersDTO createOrder(Integer userId, OrdersDTO orderDTO);
    OrdersDTO getOrderById(Integer orderId);
    List<OrdersDTO> getOrdersByUserId(Integer userId);
    OrdersDTO updateOrder(Integer orderId, OrdersDTO orderDTO);
    OrdersDTO updateOrderStatus(Integer orderId, OrdersDTO orderDTO);
    OrdersDTO cancelOrder(Integer orderId);
    void deleteOrder(Integer orderId);
}
