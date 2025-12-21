package com.hse.gozon.paymentsservice.payment;

import com.hse.gozon.paymentsservice.exception.NotFoundException;
import com.hse.gozon.paymentsservice.model.*;
import com.hse.gozon.paymentsservice.payment.serializer.PaymentEventAvroSerializer;
import com.hse.gozon.paymentsservice.payment.serializer.PaymentEventJsonSerializer;
import com.hse.gozon.paymentsservice.repository.AccountRepository;
import com.hse.gozon.paymentsservice.repository.PaymentInboxRepository;
import com.hse.gozon.paymentsservice.repository.PaymentOutboxRepository;
import com.hse.gozon.paymentsservice.repository.PaymentRepository;
import com.hse.kafka.avro.event.PaymentEventAvro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.hse.gozon.paymentsservice.mapper.PaymentMapper.toEntity;
import static com.hse.gozon.paymentsservice.mapper.PaymentOutboxMapper.toOutbox;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentService self;
    private final PaymentRepository paymentRepository;
    private final PaymentOutboxRepository outboxRepository;
    private final PaymentInboxRepository inboxRepository;
    private final AccountRepository accountRepository;
    private final PaymentEventJsonSerializer jsonSerializer;


    @Scheduled(fixedDelay = 2000)
    public void handlePaymentEvent() {
        List<PaymentInbox> inboxes = inboxRepository.findUnprocessed();
        for (PaymentInbox inbox : inboxes) {
            try {
                PaymentEventAvro paymentEvent = jsonSerializer.deserialize(inbox.getPayload());
                self.processPaymentEvent(paymentEvent, inbox.getEventId());
                inbox.setProcessedAt(LocalDateTime.now());
                inboxRepository.save(inbox);
            } catch (Exception exception) {
                log.error("произошла ошибка во время обработки платежа", exception);
            }
        }
    }

    @Transactional
    public void processPaymentEvent(PaymentEventAvro paymentEvent, String eventId) {
        Account account = getAccountById(paymentEvent.getAccountId());
        BigDecimal orderCost = BigDecimal.valueOf(paymentEvent.getAmount());
        int updatedRows = accountRepository.withDraw(account.getId(), orderCost);
        Payment payment = toEntity(paymentEvent);
        if (updatedRows == 0) {
            payment.setStatus(PaymentStatus.CANCELLED);
            log.error("недостаточно денег на аккаунте");
        } else {
            payment.setStatus(PaymentStatus.APPROVED);
        }
        paymentRepository.save(payment);
        log.debug("списание средств прошло успешно");
        PaymentOutbox outbox = toOutbox(payment, jsonSerializer.serialize(paymentEvent), eventId);
        outboxRepository.save(outbox);
    }

    private Account getAccountById(Integer id) {
        return accountRepository.findById(id).orElseThrow(() ->
                new NotFoundException("аккаунт с id" + id + " не найден"));
    }

}
