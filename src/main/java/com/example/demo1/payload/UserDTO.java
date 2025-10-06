package com.example.demo1.payload;

import lombok.Data;

@Data
public class UserDTO {
    private Integer userId;
    private String name;
    private String email;
    private String phone;
    private String address;
}
