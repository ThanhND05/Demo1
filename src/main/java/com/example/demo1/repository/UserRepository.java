package com.example.demo1.repository;

import com.example.demo1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    User findByName(String name);

    Optional<User> findByNameOrEmail(String name, String email);

    Boolean existsByName(String name); //is username present in database return true

    Boolean existsByEmail(String email);  //is email present in database return true
}
