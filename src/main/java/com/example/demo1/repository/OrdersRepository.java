package com.example.demo1.repository;

import com.example.demo1.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByUser_UserId(Integer userId);
}
