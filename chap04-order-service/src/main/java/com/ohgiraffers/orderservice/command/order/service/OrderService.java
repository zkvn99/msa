package com.ohgiraffers.orderservice.command.order.service;

import com.ohgiraffers.orderservice.command.order.client.UserClient;
import com.ohgiraffers.orderservice.command.order.dto.OrderDTO;
import com.ohgiraffers.orderservice.command.order.entity.Order;
import com.ohgiraffers.orderservice.command.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserClient userClient;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;

    public OrderDTO createOrder(OrderDTO orderDTO, Long userId) {
        // 주문 생성 전에 사용자의 등급을 알아와서 주문 생성 시 활용해야 하는 상황
        String userGrade = userClient.getUserGrade(userId).getData();
        if(userGrade == null || userGrade.isEmpty()) {
            throw new RuntimeException("유효하지 않은 사용자 등급입니다.");
        }

        // 주문 생성 로직
        if("PREMIUM".equals(userGrade)) orderDTO.setPrice(orderDTO.getPrice() * 0.9);
        Order order = modelMapper.map(orderDTO, Order.class);
        order.createOrder(userId, LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        orderDTO.setId(savedOrder.getId());
        orderDTO.setOrderDate(savedOrder.getOrderDate());
        return orderDTO;
    }
}
