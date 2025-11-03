package com.ohgiraffers.orderservice.command.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String product;
    private int quantity;
    private double price;
    private LocalDateTime orderDate;

    public void createOrder(Long userId, LocalDateTime now) {
        this.userId = userId;
        this.orderDate = now;
    }
}
