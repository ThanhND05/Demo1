package com.example.demo1.controller;

import com.example.demo1.entity.OrderItems;
import com.example.demo1.payload.OrderItemsDTO;
import com.example.demo1.service.OrderItemsService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orderItems")
public class OrderItemsController {
    @Autowired
    private OrderItemsService orderItemsService;
    
    @GetMapping
    public ResponseEntity<List<OrderItemsDTO>> getAllOrderItems() {
        return ResponseEntity.ok(orderItemsService.findAll());
    }

    @GetMapping("/{orderItemsId}")
    public ResponseEntity<OrderItemsDTO> getOrderItem(@PathVariable Integer orderItemsId) {
        return ResponseEntity.ok(orderItemsService.getOrderItem(orderItemsId));
    }

    @PostMapping
    public ResponseEntity<OrderItemsDTO> createOrderItem(@RequestBody @Valid OrderItemsDTO orderItemsDTO) {
        return new ResponseEntity<>(orderItemsService.createOrderItem(orderItemsDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{orderItemsId}")
    public ResponseEntity<Void> updateOrderItems(@PathVariable Integer orderItemsId,@RequestBody @Valid OrderItemsDTO orderItemsDTO) {
        orderItemsService.updateOrderItems(orderItemsId, orderItemsDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{orderItemsId}")
    public ResponseEntity<Void> deleteOrderItems(@PathVariable Integer orderItemsId) {
        orderItemsService.deleteOrderItems(orderItemsId);
        return ResponseEntity.noContent().build();
    }
}
