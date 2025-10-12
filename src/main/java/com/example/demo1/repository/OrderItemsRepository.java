package com.example.demo1.repository;

import com.example.demo1.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItems,Integer> {
    List<OrderItems> findByOrder_OrderId(Integer ordersId);
}
