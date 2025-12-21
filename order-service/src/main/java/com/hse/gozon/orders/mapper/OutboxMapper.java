package com.hse.gozon.orders.mapper;

import com.hse.gozon.orders.model.Order;
import com.hse.gozon.orders.model.OrderOutbox;

import java.util.UUID;

public class OutboxMapper {

    public static OrderOutbox toOutbox(Order order, String payload){
        return OrderOutbox.builder()
                .orderId(order.getId())
                .eventId(UUID.randomUUID())
                .payload(payload)
                .createdAt(order.getCreatedAt())
                .build();
    }
}
