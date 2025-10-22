package com.example.demo1.controller;

import com.example.demo1.converter.UserConverter;
import com.example.demo1.entity.Payment;
import com.example.demo1.entity.Role;
import com.example.demo1.entity.User;
import com.example.demo1.payload.UserDTO;
import com.example.demo1.repository.PaymentRepository;
import com.example.demo1.repository.RoleRepository;
import com.example.demo1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private RoleRepository roleRepository;

    private static final Integer SUPER_ADMIN_USER_ID = 1;
    @GetMapping
    public String showAdminHome() {
        return "redirect:/admin/users";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model, Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        Page<UserDTO> userDtoPage = userPage.map(userConverter::toDTO);
        model.addAttribute("users", userDtoPage);
        model.addAttribute("superAdminId", SUPER_ADMIN_USER_ID);
        return "admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        if (id.equals(SUPER_ADMIN_USER_ID)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa Super Admin!");
            return "redirect:/admin/users";
        }

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa người dùng thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy người dùng để xóa!");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/payments")
    public String getAllPaymentsAndRevenue(
                                            @PageableDefault(sort = "paymentDate", direction = Sort.Direction.DESC) Pageable pageable,
                                            Model model) {
        Page<Payment> paymentPage = paymentRepository.findAll(pageable);
        model.addAttribute("paymentPage", paymentPage);
        BigDecimal totalRevenue = paymentRepository.calculateTotalRevenue();
        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }
        model.addAttribute("totalRevenue", totalRevenue);

        return "admin/payments";
    }

    @GetMapping("/users/{id}/edit-roles")
    public String showEditRolesForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        if (id.equals(SUPER_ADMIN_USER_ID)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể chỉnh sửa vai trò của Super Admin!");
            return "redirect:/admin/users";
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user ID: " + id));
        UserDTO userDTO = userConverter.toDTO(user);
        List<Role> allRoles = roleRepository.findAll();

        model.addAttribute("user", userDTO);
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("userRoles", userDTO.getRoles());

        return "admin/edit-roles";
    }

    @PostMapping("/users/{id}/edit-roles")
    public String updateUserRoles(@PathVariable Integer id,
                                  @RequestParam(required = false) List<String> roleNames,
                                  RedirectAttributes redirectAttributes,
                                  Authentication authentication) {
        Integer currentAdminId = null;
        if (authentication != null && authentication.isAuthenticated()) {
            String currentUsername = authentication.getName();
            User currentUser = userRepository.findByEmail(currentUsername)
                    .orElse(null);
            if (currentUser != null) {
                currentAdminId = currentUser.getUserId();
            }
        }
        if (currentAdminId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi xác thực người dùng hiện tại.");
            return "redirect:/admin/users";
        }

        if (id.equals(SUPER_ADMIN_USER_ID)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể chỉnh sửa vai trò của Super Admin!");
            return "redirect:/admin/users";
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user ID: " + id));

        Set<String> currentRoleNames = user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        boolean wasAdmin = currentRoleNames.contains("ROLE_ADMIN");
        boolean willBeAdmin = (roleNames != null && roleNames.contains("ROLE_ADMIN"));

        if (wasAdmin && !willBeAdmin && !currentAdminId.equals(SUPER_ADMIN_USER_ID)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Chỉ Super Admin mới có quyền hạ cấp Admin khác!");
            return "redirect:/admin/users/" + id + "/edit-roles";
        }

        Set<Role> newRoles = new HashSet<>();
        if (roleNames != null && !roleNames.isEmpty()) {
            for (String roleName : roleNames) {
                Role role = roleRepository.findByRoleName(roleName);
                if (role != null) {
                    newRoles.add(role);
                } else {
                    System.err.println("Vai trò không hợp lệ: " + roleName);
                }
            }
        }
        user.setRoles(newRoles);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật vai trò thành công cho user " + user.getName());
        return "redirect:/admin/users";
    }
}
