package com.example.demo1.controller;

import com.example.demo1.converter.UserConverter;
import com.example.demo1.entity.User;
import com.example.demo1.payload.UserDTO;
import com.example.demo1.repository.PaymentRepository;
import com.example.demo1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/users")
    public String getAllUsers(Model model, Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        Page<UserDTO> userDtoPage = userPage.map(userConverter::toDTO);
        model.addAttribute("users", userDtoPage);
        return "admin/users"; // trỏ tới file templates/admin/users.html
    }

    @GetMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Integer id, Model model) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
        return "redirect:/admin/users"; // xóa xong quay lại trang danh sách
    }

    @GetMapping("/revenue")
    public String getTotalRevenue(Model model) {
        BigDecimal totalRevenue = paymentRepository.calculateTotalRevenue();
        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }
        model.addAttribute("totalRevenue", totalRevenue);
        return "admin/revenue"; // trỏ tới file templates/admin/revenue.html
    }
}
