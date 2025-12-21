package com.hse.gozon.orders.consumer.deserializer;

import com.hse.kafka.avro.deserializer.BaseAvroDeserializer;
import com.hse.kafka.avro.event.PaymentEventAvro;


public class PaymentEventDeserializer extends BaseAvroDeserializer<PaymentEventAvro> {
    public PaymentEventDeserializer() {
        super(PaymentEventAvro.getClassSchema());
    }
}
