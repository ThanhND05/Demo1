package com.example.demo1.converter;

import com.example.demo1.entity.Cart;
import com.example.demo1.payload.CartDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartConverter {
    @Mapping(source = "user.userId", target = "userId")
    CartDTO toDTO(Cart cart);
    @Mapping(target = "user.userId", source = "userId")
    Cart toEntity(CartDTO cartDTO);
}
