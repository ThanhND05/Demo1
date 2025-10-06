package com.example.demo1.converter;

import com.example.demo1.entity.CartItem;
import com.example.demo1.payload.CartItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductShortConverter.class})
public interface CartItemConverter {
    @Mapping(source = "cart.cartId", target = "cartId")
    @Mapping(source = "product", target = "product")
    CartItemDTO toDTO(CartItem cartItem);

    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product", source = "product")
    CartItem toEntity(CartItemDTO cartItemDTO);
}
