package com.example.demo1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @JsonIgnore
    private User user;
    
    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<OrderItems> orderItems = new HashSet<>();

    private LocalDateTime orderDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private OrderStatus status;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    private String shippingAddress;
    
    public void calculateTotalAmount() {
        if (orderItems != null) {
            this.totalAmount = orderItems.stream().map(OrderItems::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            this.totalAmount = BigDecimal.ZERO;
        }
    }
    public void addItem(OrderItems item) {
        orderItems.add(item);
        item.setOrder(this);
        calculateTotalAmount();
    }

    public void removeItem(OrderItems item) {
        orderItems.remove(item);
        item.setOrder(null);
        calculateTotalAmount();
    }
    @PrePersist
    @PreUpdate
    public void onSaveOrUpdate() {
        calculateTotalAmount();
    }
}
