package com.hse.gozon.orders.producer.deserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hse.gozon.orders.exception.OrderServiceException;
import com.hse.kafka.avro.event.OrderCreatedEventAvro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreatedEventJsonSerializer {
    private final ObjectMapper objectMapper;

    public String serialize(OrderCreatedEventAvro orderCreatedEvent) {
        try {
            return objectMapper.writeValueAsString(orderCreatedEvent);
        } catch (JsonProcessingException exception) {
            throw new SerializationException("ошибка сереализации данных заказа", exception);
        }
    }

    public OrderCreatedEventAvro deserialize(String json) {
        try {
            return objectMapper.readValue(json, OrderCreatedEventAvro.class);
        } catch (JsonProcessingException exception) {
            throw new OrderServiceException("ошибка сереализации данных заказа", exception);
        }
    }

}
