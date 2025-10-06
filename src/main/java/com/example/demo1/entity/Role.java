package com.example.demo1.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
//@AllArgsConstructor
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Column(name="roleName")
    private String roleName;

    public Role(String roleName) {
        this.roleName = roleName;
    }
}
