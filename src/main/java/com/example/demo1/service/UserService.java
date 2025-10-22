package com.example.demo1.service;

import com.example.demo1.entity.User;
import com.example.demo1.payload.SignUpDTO;

import java.util.Optional;

public interface UserService {
    void saveUser(SignUpDTO signUpDTO);

    Optional<User> findUserByEmail(String email);


}
