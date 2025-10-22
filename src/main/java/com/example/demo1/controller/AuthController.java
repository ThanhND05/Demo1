package com.example.demo1.controller;

import com.example.demo1.entity.Cart;
import com.example.demo1.entity.Role;
import com.example.demo1.entity.User;
import com.example.demo1.payload.SignUpDTO;
import com.example.demo1.repository.RoleRepository;
import com.example.demo1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new SignUpDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") SignUpDTO signUpDto, Model model, RedirectAttributes redirectAttributes) { // Thêm RedirectAttributes
        if (userRepository.existsByName(signUpDto.getUsername())) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
            return "auth/register";
        }

        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "auth/register";
        }

        User user = new User();
        user.setName(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setPhone(signUpDto.getPhone());
        user.setAddress(signUpDto.getAddress());

        String roleName = userRepository.count() == 0 ? "ROLE_ADMIN" : "ROLE_USER";
        Role role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            role = new Role(roleName);
            roleRepository.save(role);
        }

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        userRepository.save(user);


        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Hãy đăng nhập.");
        return "redirect:/auth/login";
    }

}