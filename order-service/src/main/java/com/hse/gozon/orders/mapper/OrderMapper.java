package com.hse.gozon.orders.mapper;

import com.hse.gozon.dto.OrderCreateRequestDto;
import com.hse.gozon.dto.OrderDto;
import com.hse.gozon.orders.model.Order;
import com.hse.gozon.orders.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderMapper {

    public static Order toEntity(OrderCreateRequestDto newOrder) {
        return Order.builder()
                .totalAmount(newOrder.getTotalAmount())
                .userId(newOrder.getUserId())
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();
    }

    public static OrderDto toDto(Order order) {
        return OrderDto.builder()
                .status(order.getStatus().name())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .build();
    }

    public static List<OrderDto> toDto(List<Order> orders) {
        return orders.stream().map(OrderMapper::toDto).toList();
    }
}
