package com.example.demo1.converter;

import com.example.demo1.entity.CartItem;
import com.example.demo1.payload.CartItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemConverter {
    @Mapping(source = "cart.cartId", target = "cartId")
    @Mapping(source = "product.productId", target = "productId")
    CartItemDTO toDTO(CartItem cartItem);

    @Mapping(target = "cart.cartId", source = "cartId")
    @Mapping(target = "product.productId", source = "productId")
    CartItem toEntity(CartItemDTO cartItemDTO);
}
