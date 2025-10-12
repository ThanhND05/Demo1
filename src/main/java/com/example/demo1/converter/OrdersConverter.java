package com.example.demo1.converter;

import com.example.demo1.entity.OrderItems;
import com.example.demo1.entity.Orders;
import com.example.demo1.payload.OrderItemsDTO;
import com.example.demo1.payload.OrdersDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface OrdersConverter {
    @Mapping(source = "user", target = "user")
    Orders toEntity(OrdersDTO ordersDTO);
}