package com.example.demo1.converter;

import com.example.demo1.entity.Orders;
import com.example.demo1.entity.User;
import com.example.demo1.payload.OrdersDTO;
import com.example.demo1.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring", uses = {OrderItemsConverter.class})
public abstract class OrdersConverter {

    @Autowired
    protected UserRepository userRepository;

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "orderItems", target = "orderItems")
    @Mapping(source = "orderDate", target = "orderDate")
    @Mapping(source = "totalAmount", target = "totalAmount")
    @Mapping(source = "shippingAddress", target = "shippingAddress")
    @Mapping(source = "status", target = "status")
    public abstract OrdersDTO toDTO(Orders order);

    @Mapping(target = "user", source = "userId", qualifiedByName = "userIdToUser")
    @Mapping(target = "orderItems", source = "orderItems")
    @Mapping(target = "orderDate", source = "orderDate")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "shippingAddress", source = "shippingAddress")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "orderId", ignore = true)
    public abstract Orders toEntity(OrdersDTO ordersDTO);

    @Named("userIdToUser")
    public User userIdToUser(Integer userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy User ID: " + userId));
    }
}