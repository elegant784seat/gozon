package com.hse.gozon.orders.service;

import com.hse.gozon.dto.OrderCreateRequestDto;
import com.hse.gozon.dto.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderCreateRequestDto newOrder);

    OrderDto findOrderById(Long orderId);

    List<OrderDto> findAllOrdersByAccountId(Integer accountId);
}
