package com.azizi.notification.consumer;

import com.azizi.common.constants.KafkaTopicConstants;
import com.azizi.common.payload.NotificationPayload;
import com.azizi.notification.service.NotificationService;
import com.azizi.notification.service.factory.NotificationServiceFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationServiceFactory notificationServiceFactory;

    @KafkaListener(topics = KafkaTopicConstants.PAYMENT_COMPLETED,
            containerFactory = "notificationListenerContainerFactory")
    public void consumePayment(@Payload ConsumerRecord<String, NotificationPayload> record) {
        log.info("Message Received -> record {}", record);
        NotificationPayload payload = record.value();
        NotificationService notificationService = notificationServiceFactory.getNotificationService(payload.getType());
        notificationService.addQueue(payload);
    }

}
