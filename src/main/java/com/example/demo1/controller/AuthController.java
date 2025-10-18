package com.example.demo1.controller;

import com.example.demo1.entity.Cart;
import com.example.demo1.entity.Role;
import com.example.demo1.entity.User;
import com.example.demo1.payload.SignUpDTO;
import com.example.demo1.repository.RoleRepository;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.security.JwtTokenProvider;
import com.example.demo1.service.impl.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    // ----- HIỂN THỊ FORM LOGIN -----
    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login"; // trỏ đến templates/auth/login.html
    }

    // ----- XỬ LÝ LOGIN -----
    @PostMapping("/login")
    public String loginUser(@RequestParam String usernameOrEmail,
                            @RequestParam String password,
                            Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameOrEmail, password));

            // nếu login thành công, lưu token hoặc session nếu cần
            String token = tokenProvider.generateToken(authentication);
            model.addAttribute("token", token);
            model.addAttribute("username", usernameOrEmail);

            // chuyển đến dashboard
            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu sai!");
            return "auth/login";
        }
    }

    // ----- HIỂN THỊ FORM REGISTER -----
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new SignUpDTO());
        return "auth/register"; // trỏ tới templates/auth/register.html
    }

    // ----- XỬ LÝ REGISTER -----
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") SignUpDTO signUpDto, Model model) {
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

        model.addAttribute("success", "Đăng ký thành công! Hãy đăng nhập.");
        return "auth/login";
    }

    // ----- LOGOUT -----
    @GetMapping("/logout")
    public String logoutUser(@RequestParam(required = false) String token, Model model) {
        if (token != null) {
            tokenBlacklistService.blacklistToken(token);
        }
        return "redirect:/auth/login";
    }
}
