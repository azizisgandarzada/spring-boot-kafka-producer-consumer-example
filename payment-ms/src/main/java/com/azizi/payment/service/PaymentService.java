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
        IntStream.range(1, 5000).forEach(i -> {
            EmailPayload emailPayload = EmailPayload.builder()
                    .subject("Balance Info")
                    .content("10 USD has been removed from your balance. Balance: 1000 USD")
                    .receivers(List.of("test@gmail.com"))
                    .build();
            NotificationPayload payload = NotificationPayload.builder()
                    .userId(i)
                    .type(NotificationType.EMAIL)
                    .email(emailPayload)
                    .build();
            kafkaMessageSender.sendMessage(getProducerRecord(payload, 0));
        });
    }

    @Async
    public void sendSmsWhenPaymentIsSuccessful() {
        IntStream.range(1, 5000).forEach(i -> {
            SmsPayload smsPayload = SmsPayload.builder()
                    .text("10 USD has been removed from your balance. Balance: 1000 USD")
                    .phoneNumber("test")
                    .build();
            NotificationPayload payload = NotificationPayload.builder()
                    .userId(i)
                    .type(NotificationType.SMS)
                    .sms(smsPayload)
                    .build();
            kafkaMessageSender.sendMessage(getProducerRecord(payload, 1));
        });
    }

    @Async
    public void sendMobilePushWhenPaymentIsSuccessful() {
        IntStream.range(1, 5000).forEach(i -> {
            MobilePushPayload mobilePushPayload = MobilePushPayload.builder()
                    .title("Balance Info")
                    .body("10 USD has been removed from your balance. Balance: 1000 USD")
                    .token("test")
                    .data(Map.of("amount", "5 USD"))
                    .build();
            NotificationPayload payload = NotificationPayload.builder()
                    .userId(i)
                    .type(NotificationType.MOBILE_PUSH)
                    .mobilePush(mobilePushPayload)
                    .build();
            kafkaMessageSender.sendMessage(getProducerRecord(payload, 2));
        });
    }

    private ProducerRecord<String, NotificationPayload> getProducerRecord(NotificationPayload payload,
                                                                          Integer partition) {
        return new ProducerRecord<>(KafkaTopicConstants.PAYMENT_COMPLETED, partition, null, payload);
    }

}
