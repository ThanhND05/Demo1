package com.example.demo1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;
    @OneToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Orders order;
    private String paymentMethod;
    private LocalDateTime paymentDate = LocalDateTime.now();

    private BigDecimal amount;

    private String paymentStatus;
    private String transactionId;
}