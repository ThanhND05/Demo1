package com.example.demo1.controller;

import com.example.demo1.entity.User;
import com.example.demo1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // --- Hiển thị trang thông tin người dùng hiện tại ---
    @GetMapping("/me")
    public String getCurrentUser(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "user/profile"; // templates/user/profile.html
    }

    // --- Hiển thị form cập nhật thông tin cá nhân ---
    @GetMapping("/me/edit")
    public String showEditProfileForm(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "user/edit"; // templates/user/edit.html
    }

    // --- Xử lý cập nhật thông tin cá nhân ---
    @PostMapping("/me")
    public String updateProfile(Authentication authentication, @ModelAttribute("user") User updateUser, Model model) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        if (user != null) {
            user.setName(updateUser.getName());
            user.setPhone(updateUser.getPhone());
            user.setAddress(updateUser.getAddress());
            userRepository.save(user);
            model.addAttribute("success", "Cập nhật thông tin thành công!");
        } else {
            model.addAttribute("error", "Không tìm thấy người dùng!");
        }

        model.addAttribute("user", user);
        return "user/profile"; // sau khi cập nhật, quay lại trang profile
    }
}
