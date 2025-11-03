package com.ohgiraffers.orderservice.command.order.repository;

import com.ohgiraffers.orderservice.command.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
