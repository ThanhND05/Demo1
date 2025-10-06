package com.example.demo1.converter;

import com.example.demo1.entity.User;
import com.example.demo1.payload.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserConverter {
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
}
