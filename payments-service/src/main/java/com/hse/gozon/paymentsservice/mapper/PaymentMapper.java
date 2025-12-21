package com.hse.gozon.paymentsservice.mapper;

import com.hse.gozon.paymentsservice.model.Account;
import com.hse.gozon.paymentsservice.model.Payment;
import com.hse.kafka.avro.event.PaymentEventAvro;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class PaymentMapper {
    public static Payment toEntity(PaymentEventAvro paymentEvent){
        return Payment.builder()
                .accountId(paymentEvent.getAccountId())
                .amount(BigDecimal.valueOf(paymentEvent.getAmount()))
                .createdAt(LocalDateTime.ofInstant(paymentEvent.getCreatedAt(), ZoneOffset.UTC))
                .orderId(paymentEvent.getOrderId())
                .build();
    }
}
