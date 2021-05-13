package com.azizi.payment.service;

import com.azizi.common.constants.KafkaTopicConstants;
import com.azizi.common.enums.NotificationType;
import com.azizi.common.payload.EmailPayload;
import com.azizi.common.payload.MobilePushPayload;
import com.azizi.common.payload.NotificationPayload;
import com.azizi.common.payload.SmsPayload;
import com.azizi.payment.producer.KafkaMessageSender;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final KafkaMessageSender kafkaMessageSender;

    @Async
    public void sendEmailWhenPaymentIsSuccessful() {
        IntStream.range(1, 15).forEach(i -> {
            EmailPayload emailPayload = EmailPayload.builder()
                    .subject("test")
                    .content("test")
                    .receivers(List.of("example@gmail.com"))
                    .build();
            NotificationPayload payload = NotificationPayload.builder()
                    .userId(i)
                    .type(NotificationType.EMAIL)
                    .email(emailPayload)
                    .build();
            kafkaMessageSender.sendMessage(getProducerRecord(payload));
        });
    }

    @Async
    public void sendSmsWhenPaymentIsSuccessful() {
        IntStream.range(1, 15).forEach(i -> {
            SmsPayload smsPayload = SmsPayload.builder()
                    .text("test")
                    .phoneNumber("test")
                    .build();
            NotificationPayload payload = NotificationPayload.builder()
                    .userId(i)
                    .type(NotificationType.SMS)
                    .sms(smsPayload)
                    .build();
            kafkaMessageSender.sendMessage(getProducerRecord(payload));
        });
    }

    @Async
    public void sendMobilePushWhenPaymentIsSuccessful() {
        IntStream.range(1, 15).forEach(i -> {
            MobilePushPayload mobilePushPayload = MobilePushPayload.builder()
                    .title("test")
                    .body("test")
                    .token("test")
                    .data(Map.of("test", "test"))
                    .build();
            NotificationPayload payload = NotificationPayload.builder()
                    .userId(i)
                    .type(NotificationType.MOBILE_PUSH)
                    .mobilePush(mobilePushPayload)
                    .build();
            kafkaMessageSender.sendMessage(getProducerRecord(payload));
        });
    }

    private ProducerRecord<String, NotificationPayload> getProducerRecord(NotificationPayload payload) {
        return new ProducerRecord<>(KafkaTopicConstants.PAYMENT_COMPLETED, null, payload);
    }

}
