package com.hse.gozon.orders.service.impl;

import com.hse.gozon.dto.OrderCreateRequestDto;
import com.hse.gozon.dto.OrderDto;
import com.hse.gozon.orders.exception.NotFoundException;
import com.hse.gozon.orders.mapper.OrderAvroMapper;
import com.hse.gozon.orders.model.Order;
import com.hse.gozon.orders.model.OrderOutbox;
import com.hse.gozon.orders.producer.deserializer.OrderCreatedEventJsonSerializer;
import com.hse.gozon.orders.repository.OrderOutboxRepository;
import com.hse.gozon.orders.repository.OrderRepository;
import com.hse.gozon.orders.service.OrderService;
import com.hse.kafka.avro.event.OrderCreatedEventAvro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hse.gozon.orders.mapper.OrderMapper.toDto;
import static com.hse.gozon.orders.mapper.OrderMapper.toEntity;
import static com.hse.gozon.orders.mapper.OutboxMapper.toOutbox;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderOutboxRepository outboxRepository;
    private final OrderCreatedEventJsonSerializer jsonSerializer;

    @Override
    @Transactional
    public OrderDto createOrder(OrderCreateRequestDto newOrder) {
        Order order = orderRepository.save(toEntity(newOrder));

        OrderCreatedEventAvro event = OrderAvroMapper.toAvro(order);

        OrderOutbox outbox = toOutbox(order, jsonSerializer.serialize(event));
        outboxRepository.save(outbox);
        log.debug("заказ с id{} успешно сохранен", order.getId());
        return toDto(order);
    }

    @Override
    public OrderDto findOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException("пользователь с id " + orderId + " не найден"));
        log.debug("пользователь с id{} успешно найден", order);
        return toDto(order);
    }

    @Override
    public List<OrderDto> findAllOrdersByAccountId(Integer accountId) {
        return toDto(orderRepository.findAllByUserId(accountId));
    }
}
