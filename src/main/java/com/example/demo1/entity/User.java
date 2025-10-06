package com.example.demo1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
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
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Cart cart;


    public User(String name, String email, String password, Set<Role> roles, String phone, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.phone = phone;
        this.address=address;
    }
}
