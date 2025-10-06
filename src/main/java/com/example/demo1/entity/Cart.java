package com.example.demo1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;
    private LocalDateTime createdAt =  LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "userId", nullable = false, unique = true,
        foreignKey = @ForeignKey(name = "FK_cart_user"))
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<CartItem> items;
}
