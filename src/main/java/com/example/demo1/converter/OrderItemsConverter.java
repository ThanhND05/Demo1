package com.example.demo1.converter;

import com.example.demo1.entity.OrderItems;
import com.example.demo1.payload.OrderItemsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") 
public interface OrderItemsConverter {
    
    @Mapping(source = "order.orderId", target = "orderId")
    @Mapping(source = "product.productId", target = "productId")
    OrderItemsDTO toDTO(OrderItems orderItem);
    
    @Mapping(target = "order", ignore = true) 
    @Mapping(target = "product", ignore = true) 
    OrderItems toEntity(OrderItemsDTO orderItemsDTO);
}