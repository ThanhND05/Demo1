package com.example.demo1.service;

import com.example.demo1.entity.OrderItems;
import com.example.demo1.payload.OrderItemsDTO;
import java.util.List;

public interface OrderItemsService {
    List<OrderItemsDTO> findAll();
    OrderItemsDTO getOrderItem(Integer orderItemId);
    List<OrderItemsDTO> findByOrderId(Integer ordersId);
    OrderItemsDTO createOrderItem(OrderItemsDTO orderItemsDTO);
    OrderItemsDTO updateOrderItems(Integer orderItemId, OrderItemsDTO orderItemsDTO);
    void deleteOrderItems(Integer orderItemsId);
}
