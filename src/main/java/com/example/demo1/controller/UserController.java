package com.example.demo1.controller;

import com.example.demo1.entity.User;
import com.example.demo1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private User getUserFromAuthentication(Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("Người dùng chưa được xác thực.");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với email: " + email));
    }

    @GetMapping("/me")
    public String getCurrentUser(Authentication authentication, Model model) {
        try {
            User user = getUserFromAuthentication(authentication);
            model.addAttribute("user", user);
            return "user/profile";
        } catch (RuntimeException e) {
            return "redirect:/auth/login?error=user_not_found";
        }
    }

    @GetMapping("/me/edit")
    public String showEditProfileForm(Authentication authentication, Model model) {
        try {
            User user = getUserFromAuthentication(authentication);
            model.addAttribute("user", user);
            return "user/edit";
        } catch (RuntimeException e) {
            return "redirect:/auth/login?error=user_not_found";
        }
    }

    @PostMapping("/me")
    public String updateProfile(Authentication authentication,
                                @RequestParam String name,
                                @RequestParam String phone,
                                @RequestParam String address,
                                RedirectAttributes redirectAttributes) {
        try {
            User userInDb = getUserFromAuthentication(authentication);
            userInDb.setName(name);
            userInDb.setPhone(phone);
            userInDb.setAddress(address);
            userRepository.save(userInDb);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi cập nhật: " + e.getMessage());
        }

        return "redirect:/user/me";

    }
}
