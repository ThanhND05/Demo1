package com.example.demo1.service.impl;

import com.example.demo1.converter.OrderItemsConverter;
import com.example.demo1.entity.OrderItems;
import com.example.demo1.entity.OrderStatus;
import com.example.demo1.entity.Orders;
import com.example.demo1.entity.Product;
import com.example.demo1.payload.OrderItemsDTO;
import com.example.demo1.repository.OrderItemsRepository;
import com.example.demo1.repository.OrdersRepository;
import com.example.demo1.repository.ProductRepository;
import com.example.demo1.service.OrderItemsService;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemsServiceImpl implements OrderItemsService{
    @Autowired
    private OrderItemsRepository orderItemsRepository;
    @Autowired
    private OrderItemsConverter orderItemsConverter;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    public List<OrderItemsDTO> findAll() {
        return orderItemsRepository.findAll().stream().map(orderItemsConverter::toDTO).collect(Collectors.toList());
    }
    
    @Override
    public OrderItemsDTO getOrderItem(Integer orderItemId){
        OrderItems oderItems = orderItemsRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("Order item not found!"));
        return orderItemsConverter.toDTO(oderItems);
    }
    @Override
    public List<OrderItemsDTO> findByOrderId(Integer ordersId) {
        List<OrderItems> items = orderItemsRepository.findByOrder_OrderId(ordersId);
        return items.stream().map(orderItemsConverter::toDTO).collect(Collectors.toList());
    }
    
    @Override
    public OrderItemsDTO createOrderItem(OrderItemsDTO orderItemsDTO){
        Orders order = ordersRepository.findById(orderItemsDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found!"));
        Product product = productRepository.findById(orderItemsDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found!"));
        OrderItems item = orderItemsConverter.toEntity(orderItemsDTO);
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(orderItemsDTO.getQuantity());
        item.setPrice(product.getPrice());
        item.setSubTotal(product.getPrice().multiply(BigDecimal.valueOf(orderItemsDTO.getQuantity())));
        OrderItems saved = orderItemsRepository.save(item);
        order.calculateTotalAmount();
        ordersRepository.save(order);
        return orderItemsConverter.toDTO(saved);
    }
    
    @Override
    public OrderItemsDTO updateOrderItems(Integer orderItemId, OrderItemsDTO orderItemsDTO){
        OrderItems item = orderItemsRepository.findById(orderItemId)
            .orElseThrow(() -> new RuntimeException("Order item not found!"));
        Orders order = item.getOrder();
        OrderStatus status = order.getStatus();
        if (!(status == OrderStatus.PENDING)) {
            throw new RuntimeException("Cannot update order items because the order is already " + status);
        }
        if (orderItemsDTO.getQuantity() != null && orderItemsDTO.getQuantity() > 0) {
            item.setQuantity(orderItemsDTO.getQuantity());
        }
        OrderItems updated = orderItemsRepository.save(item);
        order.calculateTotalAmount();
        ordersRepository.save(order);
        return orderItemsConverter.toDTO(updated);
    }
    @Override
    public void deleteOrderItems(Integer orderItemId) {
        OrderItems item = orderItemsRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("Order item not found!"));
        Orders order = item.getOrder();
        orderItemsRepository.delete(item);
        order.calculateTotalAmount();
        ordersRepository.save(order);
    }
    
    
}
