package com.azizi.notification.service;

import com.azizi.common.payload.NotificationPayload;
import com.azizi.common.payload.SmsPayload;
import com.azizi.notification.document.Sms;
import com.azizi.notification.enums.NotificationStatus;
import com.azizi.notification.repository.SmsRepository;
import java.time.LocalDateTime;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class SmsService implements NotificationService<Sms> {

    private static final Integer RETRY_LIMIT = 5;

    private final SmsRepository smsRepository;
    private final AsyncService asyncService;

    @Override
    public void createAndSend(NotificationPayload payload) {
        SmsPayload smsPayload = payload.getSms();
        Sms sms = Sms.builder()
                .userId(payload.getUserId())
                .status(NotificationStatus.CREATED)
                .text(smsPayload.getText())
                .phoneNumber(smsPayload.getPhoneNumber())
                .build();
        smsRepository.save(sms);
        log.info("Sms created -> sms: {}", sms);
        try {
            asyncService.sendSms(sms);
        } catch (RejectedExecutionException ex) {
            waitAndResendAsync(sms, 5, 1, TimeUnit.SECONDS);
        }
    }

    @Override
    public void send(Sms sms) {
        sms.setStatus(NotificationStatus.PROCESSING);
        smsRepository.save(sms);
        log.info("Sms sending -> sms: {}", sms);
        sms.setStatus(NotificationStatus.SENT);
        sms.setSendAt(LocalDateTime.now());
        smsRepository.save(sms);
        log.info("Sms sent -> sms: {}", sms);
    }

    private void waitAndResendAsync(Sms sms, int time, int retryCount, TimeUnit timeUnit) {
        if (retryCount > RETRY_LIMIT) {
            sms.setStatus(NotificationStatus.FAILED);
            smsRepository.save(sms);
            log.error("Sms can not be send -> sms: {}, retryCount: {}, time: {}, timeUnit: {}", sms, retryCount, time,
                    timeUnit);
            return;
        }
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        try {
            asyncService.sendSms(sms);
        } catch (RejectedExecutionException ex) {
            retryCount++;
            waitAndResendAsync(sms, time, retryCount, timeUnit);
        }
    }

}
