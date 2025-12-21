package com.hse.gozon.paymentsservice.payment;

import com.hse.gozon.paymentsservice.model.PaymentInbox;
import com.hse.gozon.paymentsservice.payment.serializer.PaymentEventJsonSerializer;
import com.hse.gozon.paymentsservice.repository.PaymentInboxRepository;
import com.hse.kafka.avro.event.OrderCreatedEventAvro;
import com.hse.kafka.avro.event.PaymentEventAvro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.hse.gozon.paymentsservice.mapper.PaymentInboxMapper.toPaymentInbox;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentInboxPoller {
    private final PaymentInboxRepository inboxRepository;
    private final Deserializer<OrderCreatedEventAvro> orderCreatedEventAvroDeserializer;
    private final KafkaConsumer<String, OrderCreatedEventAvro> orderEventConsumer;
    private final PaymentEventJsonSerializer jsonSerializer;

    private final Duration POLL_DURATION = Duration.ofMillis(2000);

    @Value("${spring.kafka.topics.gozon.orders.v1}")
    private String ordersTopic;

    @Scheduled(fixedDelay = 2000)
    public void poll() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(orderEventConsumer::wakeup));
            orderEventConsumer.subscribe(List.of(ordersTopic));
            ConsumerRecords<String, OrderCreatedEventAvro> records = orderEventConsumer.poll(POLL_DURATION);
            for (ConsumerRecord<String, OrderCreatedEventAvro> record : records) {
                processEvent(record.value(), record.key());
            }
        } catch (WakeupException exception){

        } catch (Exception exception){
            log.error("произошла ошибка при обработке данных", exception);
        }
    }

    private void processEvent(OrderCreatedEventAvro orderEvent, String eventId) {
        PaymentEventAvro paymentEventAvro = PaymentEventAvro.newBuilder()
                .setAmount(orderEvent.getAmount())
                .setAccountId(orderEvent.getUserId())
                .setOrderId(orderEvent.getOrderId())
                .setCreatedAt(LocalDateTime.now().toInstant(ZoneOffset.UTC))
                .build();

        PaymentInbox inbox = toPaymentInbox(jsonSerializer.serialize(paymentEventAvro), eventId);
        inboxRepository.save(inbox);
    }
}
