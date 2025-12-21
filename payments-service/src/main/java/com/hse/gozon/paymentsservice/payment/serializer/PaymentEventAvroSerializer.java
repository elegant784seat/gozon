package com.hse.gozon.paymentsservice.payment.serializer;

import com.hse.kafka.avro.event.PaymentEventAvro;
import com.hse.kafka.avro.serializer.BaseAvroSerializer;

public class PaymentEventAvroSerializer extends BaseAvroSerializer<PaymentEventAvro> {
    public PaymentEventAvroSerializer() {
        super(PaymentEventAvro.getClassSchema());
    }
}
