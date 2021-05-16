package com.azizi.notification.consumer;

import com.azizi.common.constants.KafkaTopicConstants;
import com.azizi.common.payload.NotificationPayload;
import com.azizi.notification.service.EmailService;
import com.azizi.notification.service.MobilePushService;
import com.azizi.notification.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaNotificationConsumer {

    private final EmailService emailService;
    private final SmsService smsService;
    private final MobilePushService mobilePushService;

    @KafkaListener(topicPartitions = @TopicPartition(topic = KafkaTopicConstants.PAYMENT_COMPLETED, partitions =
            {"0"}), containerFactory = "notificationListenerContainerFactory")
    public void consumeEmail(@Payload ConsumerRecord<String, NotificationPayload> record) {
        log.info("Email received -> record: {}", record);
        emailService.createAndSend(record.value());
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = KafkaTopicConstants.PAYMENT_COMPLETED, partitions =
            {"1"}), containerFactory = "notificationListenerContainerFactory")
    public void consumeSms(@Payload ConsumerRecord<String, NotificationPayload> record) {
        log.info("Sms received -> record: {}", record);
        smsService.createAndSend(record.value());
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = KafkaTopicConstants.PAYMENT_COMPLETED, partitions =
            {"2"}), containerFactory = "notificationListenerContainerFactory")
    public void consumeMobilePush(@Payload ConsumerRecord<String, NotificationPayload> record) {
        log.info("Mobile PushNotification received -> record: {}", record);
        mobilePushService.createAndSend(record.value());
    }

}
