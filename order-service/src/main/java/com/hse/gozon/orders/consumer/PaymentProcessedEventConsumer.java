package com.hse.gozon.orders.consumer;

import com.hse.gozon.orders.exception.NotFoundException;
import com.hse.gozon.orders.model.Order;
import com.hse.gozon.orders.model.OrderStatus;
import com.hse.gozon.orders.repository.OrderRepository;
import com.hse.kafka.avro.event.PaymentEventAvro;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentProcessedEventConsumer {
    private final KafkaConsumer<String, PaymentEventAvro> paymentEventKafkaConsumer;
    private final Duration POLL_DURATION = Duration.ofMillis(5000);
    private final OrderRepository orderRepository;

    @Value("${spring.kafka.topics.gozon.payments.v1}")
    private String paymentTopic;

    @PostConstruct
    private void setup() {
        Runtime.getRuntime().addShutdownHook(new Thread(paymentEventKafkaConsumer::wakeup));
        paymentEventKafkaConsumer.subscribe(List.of(paymentTopic));
    }

    @Scheduled(fixedDelay = 2000)
    public void pollPaymentEvent() {
        try {
            ConsumerRecords<String, PaymentEventAvro> records = paymentEventKafkaConsumer.poll(POLL_DURATION);
            for (ConsumerRecord<String, PaymentEventAvro> record : records) {
                try {
                    setOrderStatus(record.value());
                } catch (NotFoundException exception) {
                    log.error("not found", exception);
                } catch (Exception exception){
                    log.error("error", exception);
                }
            }
            paymentEventKafkaConsumer.commitSync();
        } catch (WakeupException wakeupException) {
            paymentEventKafkaConsumer.commitSync();
            throw wakeupException;
        } catch (Exception exception) {
            paymentEventKafkaConsumer.commitSync();
        }
    }


    private void setOrderStatus(PaymentEventAvro paymentEvent) {
        Order order = orderRepository.findById(paymentEvent.getOrderId()).orElseThrow(() ->
                new NotFoundException("заказ с id" + paymentEvent.getOrderId() + " не найден"));
        switch (paymentEvent.getStatus()) {
            case APPROVED -> order.setStatus(OrderStatus.FINISHED);
            case CANCELLED -> order.setStatus(OrderStatus.CANCELLED);
        }
        orderRepository.save(order);
    }
}
