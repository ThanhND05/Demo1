package com.example.demo1.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Integer userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    @JsonIgnore
    private CartDTO cart;
}
