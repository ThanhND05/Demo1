package com.example.demo1.converter;

import com.example.demo1.entity.Orders;
import com.example.demo1.payload.OrdersDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemsConverter.class, UserConverter.class}) // Thêm UserConverter nếu bạn có
public interface OrdersConverter {

    @Mapping(source = "user", target = "user")
    @Mapping(source = "orderItems", target = "orderItems")
    OrdersDTO toDTO(Orders order);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    Orders toEntity(OrdersDTO ordersDTO);
}


