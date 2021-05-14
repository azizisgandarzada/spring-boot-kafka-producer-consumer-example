package com.azizi.notification.service;

import com.azizi.common.payload.NotificationPayload;
import com.azizi.common.payload.SmsPayload;
import com.azizi.notification.document.Sms;
import com.azizi.notification.enums.NotificationStatus;
import com.azizi.notification.repository.SmsRepository;
import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class SmsService implements NotificationService<Sms> {

    private final LinkedBlockingQueue<Sms> smsLinkedBlockingQueue;
    private final SmsRepository smsRepository;
    private final AsyncService asyncService;

    @Override
    public void addQueue(NotificationPayload payload) {
        SmsPayload smsPayload = payload.getSms();
        Sms sms = Sms.builder()
                .userId(payload.getUserId())
                .status(NotificationStatus.IN_QUEUE)
                .text(smsPayload.getText())
                .phoneNumber(smsPayload.getPhoneNumber())
                .build();
        smsRepository.save(sms);
        try {
            smsLinkedBlockingQueue.put(sms);
            log.info("Sms put to queue {}", sms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void processNotification() {
        while (true) {
            if (smsLinkedBlockingQueue.isEmpty()) {
                continue;
            }
            try {
                Sms sms = smsLinkedBlockingQueue.take();
                log.info("Sms taken from queue {}", sms);
                smsRepository.findById(sms.getId()).ifPresent(s -> {
                    s.setStatus(NotificationStatus.PROCESSING);
                    s.setProcessedAt(LocalDateTime.now());
                    smsRepository.save(s);
                    asyncService.sendSms(s);
                });
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void send(Sms notification) {
        smsRepository.findById(notification.getId()).ifPresent(sms -> {
            log.info("Sms sending... {}", sms);
            sms.setStatus(NotificationStatus.SENT);
            sms.setSendAt(LocalDateTime.now());
            smsRepository.save(sms);
            log.info("Sms sent {}", sms);
        });
    }

}
