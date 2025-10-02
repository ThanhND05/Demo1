package com.example.demo1.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class SignUpDTO {


    @NotNull(message = "Please enter a valid username.")
    private String username;

    @NotNull(message = "Please enter a valid email.")
    @Email
    private String email;

    @NotEmpty(message = "Please enter a valid password.")
    private String password;

    @NotEmpty(message = "Please enter a valid password.")
    private String phone;

    @NotEmpty(message = "Please enter a valid password.")
    private String address;


}
