package com.example.demo1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name="users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}),
        @UniqueConstraint(columnNames = {"email"})
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    @Column(updatable = false)
    private LocalDate createdAt = LocalDate.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "userId", // cột trong bảng user_roles tham chiếu đến users
                    referencedColumnName = "userId", // PK của bảng users
                    foreignKey = @ForeignKey(name = "FK_user_roles_user")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "roleId", // cột trong bảng user_roles tham chiếu đến roles
                    referencedColumnName = "roleId", // PK của bảng roles
                    foreignKey = @ForeignKey(name = "FK_user_roles_role")
            )
    )
    private Set<Role> roles;

    public User(String name, String email, String password, Set<Role> roles, String phone, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.phone = phone;
        this.address=address;
    }
}
