package com.hse.gozon.paymentsservice.mapper;

import com.hse.gozon.paymentsservice.model.PaymentInbox;

import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentInboxMapper {
    public static PaymentInbox toPaymentInbox(String payload, String eventId) {
        return PaymentInbox.builder()
                .payload(payload)
                .eventId(eventId)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
