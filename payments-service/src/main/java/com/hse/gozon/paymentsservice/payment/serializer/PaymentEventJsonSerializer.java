package com.hse.gozon.paymentsservice.payment.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hse.gozon.paymentsservice.exception.PaymentServiceException;
import com.hse.kafka.avro.event.PaymentEventAvro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventJsonSerializer {
    private final ObjectMapper objectMapper;

    public String serialize(PaymentEventAvro paymentEvent){
        try{
            return objectMapper.writeValueAsString(paymentEvent);
        } catch (JsonProcessingException exception){
            throw new PaymentServiceException("ошибка при сериализации данных", exception);
        }
    }

    public PaymentEventAvro deserialize(String json){
        try{
            return objectMapper.readValue(json, PaymentEventAvro.class);
        }catch (JsonProcessingException exception){
            throw new PaymentServiceException("ошибка при сериализации данных", exception);
        }
    }
}
