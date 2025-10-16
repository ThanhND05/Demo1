package com.example.demo1.controller;
import com.example.demo1.converter.UserConverter;
import com.example.demo1.entity.User;
import com.example.demo1.payload.UserDTO;
import com.example.demo1.repository.PaymentRepository;
import com.example.demo1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private PaymentRepository paymentRepository;
    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        // JpaRepository đã có sẵn phương thức findAll(Pageable)
        Page<User> userPage = userRepository.findAll(pageable);
        Page<UserDTO> userDtoPage = userPage.map(userConverter::toDTO);
        return ResponseEntity.ok(userDtoPage);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
        @GetMapping("/revenue")
    public ResponseEntity<Map<String, Object>> getTotalRevenue() {
        BigDecimal totalRevenue = paymentRepository.calculateTotalRevenue();
        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("totalRevenue", totalRevenue);
        return ResponseEntity.ok(response);
    }


}
