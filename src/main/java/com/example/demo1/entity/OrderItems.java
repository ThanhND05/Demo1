package com.example.demo1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orderitems")
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItemId; 
  
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", nullable = false)
    @JsonBackReference
    @JsonIgnore
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    @JsonBackReference
    private Product product;

    private Integer quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    @Column(precision = 10, scale = 2)
    private BigDecimal subTotal;

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateSubTotal();
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        calculateSubTotal();
    }

    public void calculateSubTotal() {
        if (this.price != null && this.quantity != null && this.quantity > 0) {
            this.subTotal = this.price.multiply(BigDecimal.valueOf(this.quantity));
        } else {
            this.subTotal = BigDecimal.ZERO;
        }
    }
}
