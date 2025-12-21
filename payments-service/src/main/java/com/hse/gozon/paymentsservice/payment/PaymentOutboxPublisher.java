package com.hse.gozon.paymentsservice.payment;

import com.hse.gozon.paymentsservice.exception.PaymentServiceException;
import com.hse.gozon.paymentsservice.model.PaymentOutbox;
import com.hse.gozon.paymentsservice.payment.serializer.PaymentEventJsonSerializer;
import com.hse.gozon.paymentsservice.repository.PaymentOutboxRepository;
import com.hse.kafka.avro.event.PaymentEventAvro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentOutboxPublisher {
    private final PaymentOutboxRepository outboxRepository;
    private final PaymentEventJsonSerializer jsonSerializer;
    private final KafkaProducer<String, PaymentEventAvro> paymentEventKafkaProducer;

    @Value("${spring.kafka.topics.gozon.payments.v1}")
    private String paymentTopic;

    @Scheduled(fixedDelay = 2000)
    public void publish() {
        List<PaymentOutbox> paymentOutbox = outboxRepository.findUnprocessed();
        for (PaymentOutbox outbox : paymentOutbox) {
            try {
                PaymentEventAvro paymentEvent = jsonSerializer.deserialize(outbox.getPayload());
                ProducerRecord<String, PaymentEventAvro> record = new ProducerRecord<>(paymentTopic, outbox.getEventId(), paymentEvent);
                paymentEventKafkaProducer.send(record).get(5, TimeUnit.SECONDS);
                markAsProcessed(outbox);
            } catch (Exception exception) {
                log.error("произошла ошибка во время отправки данных в топик" + paymentTopic, exception);
            }
        }
    }

    public void markAsProcessed(PaymentOutbox outbox) {
        outbox.setProcessedAt(LocalDateTime.now());
        outboxRepository.save(outbox);
    }

}



