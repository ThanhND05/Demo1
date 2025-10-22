package com.example.demo1.repository;

import com.example.demo1.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(String roleName);
    Role findByRoleId(Integer roleId);
    List<Role> findAll();
}
