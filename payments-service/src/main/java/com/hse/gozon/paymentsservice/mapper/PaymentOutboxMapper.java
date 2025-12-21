package com.hse.gozon.paymentsservice.mapper;

import com.hse.gozon.paymentsservice.model.Payment;
import com.hse.gozon.paymentsservice.model.PaymentOutbox;

public class PaymentOutboxMapper {
    public static PaymentOutbox toOutbox(Payment payment, String payload, String key) {
        return PaymentOutbox.builder()
                .paymentId(payment.getId())
                .payload(payload)
                .createdAt(payment.getCreatedAt())
                .eventId(key)
                .build();
    }
}
