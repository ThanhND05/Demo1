package com.example.demo1.controller;

import com.example.demo1.payload.OrdersDTO;
import com.example.demo1.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("/user/{userId}")
    public OrdersDTO createOrder(@PathVariable Integer userId, @RequestBody OrdersDTO orderDTO) {
        return ordersService.createOrder(userId, orderDTO);
    }

    @GetMapping("/{orderId}")
    public OrdersDTO getOrderById(@PathVariable Integer orderId) {
        return ordersService.getOrderById(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<OrdersDTO> getOrdersByUserId(@PathVariable Integer userId) {
        return ordersService.getOrdersByUserId(userId);
    }

    @PutMapping("/{orderId}/update")
    public OrdersDTO updateOrder(@PathVariable Integer orderId, @RequestBody OrdersDTO orderDTO) {
        return ordersService.updateOrder(orderId, orderDTO);
    }
    
    @PutMapping("/{orderId}/updatestatus")
    public OrdersDTO updateOrderStatus(@PathVariable Integer orderId, @RequestBody OrdersDTO orderDTO){
        return ordersService.updateOrderStatus(orderId, orderDTO);
    }

    @PutMapping("/{orderId}/cancel")
    public OrdersDTO cancelOrder(@PathVariable Integer orderId) {
        return ordersService.cancelOrder(orderId);
    }

    @DeleteMapping("/{orderId}")
    public String deleteOrder(@PathVariable Integer orderId) {
        ordersService.deleteOrder(orderId);
        return "Order deleted successfully!";
    }
}
