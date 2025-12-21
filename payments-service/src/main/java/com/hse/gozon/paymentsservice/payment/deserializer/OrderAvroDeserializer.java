package com.hse.gozon.paymentsservice.payment.deserializer;

import com.hse.kafka.avro.deserializer.BaseAvroDeserializer;
import com.hse.kafka.avro.event.OrderCreatedEventAvro;

public class OrderAvroDeserializer extends BaseAvroDeserializer<OrderCreatedEventAvro> {
    public OrderAvroDeserializer(){
        super(OrderCreatedEventAvro.getClassSchema());
    }

}
