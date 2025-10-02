package com.example.demo1.service;

import com.example.demo1.entity.User;
import com.example.demo1.payload.SignUpDTO;

public interface UserService {
    void saveUser(SignUpDTO signUpDTO);

    User findUserByEmail(String email);


}
