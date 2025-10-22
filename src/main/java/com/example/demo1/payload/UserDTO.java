package com.example.demo1.payload;

import com.example.demo1.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private Integer userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Set<Role> roles;
    @JsonIgnore
    private CartDTO cart;
}
