package com.example.demo1.converter;

import com.example.demo1.entity.Cart;
import com.example.demo1.payload.CartDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CartItemConverter.class, UserConverter.class})
public interface CartConverter {

    CartDTO toDTO(Cart cart);
    @Mapping(target = "user", ignore = true)
    Cart toEntity(CartDTO cartDTO);
}
