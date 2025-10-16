package com.example.demo1.controller;

import com.example.demo1.entity.Cart;
import com.example.demo1.entity.Role;
import com.example.demo1.entity.User;
import com.example.demo1.payload.JWTAuthResponse;
import com.example.demo1.payload.LoginDTO;
import com.example.demo1.payload.SignUpDTO;
import com.example.demo1.repository.RoleRepository;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.security.JwtTokenProvider;
import com.example.demo1.service.impl.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
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

    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Get token from tokenProvider
        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDTO signUpDto) {

        // Check if username exists in the DB
        if (userRepository.existsByName((signUpDto.getUsername()))) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        // Check if email exists in the DB
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        // Create user object
        User user = new User();
        user.setName(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setPhone(signUpDto.getPhone());
        user.setAddress(signUpDto.getAddress());

        String roleName;
        if (userRepository.count() == 0) {
            // Người dùng đầu tiên sẽ là ADMIN
            roleName = "ROLE_ADMIN";
        } else {
            // Những người dùng sau sẽ là USER
            roleName = "ROLE_USER";
        }

        Role role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            // Create a default role when "ROLE_ADMIN" is not found
            role = new Role(roleName);
            roleRepository.save(role);
        }

        // Set the roles as a HashSet of Role objects
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        userRepository.save(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> adminPanel() {
        return ResponseEntity.ok("Admin Panel");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        // Perform logout actions here, invalidate token, etc.
        // For this example, we'll just return a success message.
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(token);
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.badRequest().body("No token found in request");
    }
}
