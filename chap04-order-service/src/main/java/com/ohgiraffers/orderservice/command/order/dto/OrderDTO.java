package com.ohgiraffers.orderservice.command.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private String product;
    private int quantity;
    private double price;
    private LocalDateTime orderDate;
}
