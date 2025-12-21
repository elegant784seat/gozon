package com.hse.gozon.orders.producer;

import com.hse.gozon.orders.model.OrderOutbox;
import com.hse.gozon.orders.producer.deserializer.OrderCreatedEventJsonSerializer;
import com.hse.gozon.orders.repository.OrderOutboxRepository;
import com.hse.kafka.avro.event.OrderCreatedEventAvro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderOutboxPoller {
    private final KafkaProducer<String, OrderCreatedEventAvro> orderKafkaProducer;
    private final OrderOutboxRepository outboxRepository;
    private final OrderCreatedEventJsonSerializer jsonSerializer;

    @Value("${gozon.orders.v1}")
    private String orderTopic;

    @Scheduled(fixedDelay = 2000)
    public void publish() {
        List<OrderOutbox> unprocessedOrders = outboxRepository.findUnprocessed();
        try {
            for (OrderOutbox unprocessedOrder : unprocessedOrders) {
                OrderCreatedEventAvro event = jsonSerializer.deserialize(unprocessedOrder.getPayload());
                ProducerRecord<String, OrderCreatedEventAvro> record = new ProducerRecord<>(unprocessedOrder.getEventId().toString(), event);
                orderKafkaProducer.send(record).get(5, TimeUnit.SECONDS);
                unprocessedOrder.setProcessedAt(LocalDateTime.now());
                outboxRepository.save(unprocessedOrder);
            }
        } catch (Exception exception) {
            log.warn("произошла ошибка при отправке данных в топик " + orderTopic, exception);
        }
    }
}
