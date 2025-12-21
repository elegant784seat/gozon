package com.hse.gozon.orders.producer.serializer;

import com.hse.kafka.avro.event.OrderCreatedEventAvro;
import com.hse.kafka.avro.serializer.BaseAvroSerializer;

public class OrderCreatedEventSerializer extends BaseAvroSerializer<OrderCreatedEventAvro> {
    public OrderCreatedEventSerializer() {
        super(OrderCreatedEventAvro.getClassSchema());
    }
}
