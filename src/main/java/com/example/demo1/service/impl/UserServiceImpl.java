package com.example.demo1.service.impl;

import com.example.demo1.entity.Role;
import com.example.demo1.entity.User;
import com.example.demo1.payload.SignUpDTO;
import com.example.demo1.repository.RoleRepository;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.service.UserService;
import com.example.demo1.utils.TableConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(SignUpDTO signUpDTO) {
        Role role = roleRepository.findByRoleName(TableConstants.ADMIN);

        if (role == null) {
            role = new Role(TableConstants.ADMIN);
            roleRepository.save(role);
        }

        // Create a new HashSet and add the role to it
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User(signUpDTO.getUsername(), signUpDTO.getEmail(),
                passwordEncoder.encode(signUpDTO.getPassword()),
                roles, signUpDTO.getPhone(), signUpDTO.getAddress());

        userRepository.save(user);
    }


    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
