package com.example.demo1.converter;

import com.example.demo1.entity.User;
import com.example.demo1.payload.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserConverter {
    @Mapping(target = "cart", ignore = true)
    UserDTO toDTO(User user);
    @Mapping(target = "cart", ignore = true)
    User toEntity(UserDTO userDTO);
}
