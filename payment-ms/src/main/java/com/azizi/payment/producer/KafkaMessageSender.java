package com.azizi.payment.producer;

import com.azizi.common.payload.NotificationPayload;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class KafkaMessageSender {

    KafkaTemplate<String, NotificationPayload> kafkaTemplate;

    public void sendMessage(ProducerRecord<String, NotificationPayload> record) {
        ListenableFuture<SendResult<String, NotificationPayload>> future = kafkaTemplate.send(record);
        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<String, NotificationPayload> result) {
                log.info("Message added to queue -> record {} result {}", record, result);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Occurred exception while adding record to queue -> record {} ", record, ex);
            }
        });
    }

    public void sendMessage(String topic, NotificationPayload payload) {
        ListenableFuture<SendResult<String, NotificationPayload>> future = kafkaTemplate.send(topic, payload);
        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<String, NotificationPayload> result) {
                log.info("Message added to queue -> record {} result {}", payload, result);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Occurred exception while adding record to queue -> record {} ", payload, ex);
            }
        });
    }

}
