package com.example.demo1.payload;


import lombok.Data;

@Data
public class LoginDTO {
    private String usernameOrEmail;
    private String password;

}
