package com.azizi.notification.service;

import com.azizi.common.payload.MobilePushPayload;
import com.azizi.common.payload.NotificationPayload;
import com.azizi.notification.document.MobilePush;
import com.azizi.notification.enums.NotificationStatus;
import com.azizi.notification.repository.MobilePushRepository;
import java.time.LocalDateTime;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class MobilePushService {

    private static final Integer RETRY_LIMIT = 5;

    private final MobilePushRepository mobilePushRepository;
    private final AsyncService asyncService;

    public void createAndSend(NotificationPayload payload) {
        MobilePushPayload mobilePushPayload = payload.getMobilePush();
        MobilePush mobilePush = MobilePush.builder()
                .userId(payload.getUserId())
                .status(NotificationStatus.CREATED)
                .title(mobilePushPayload.getTitle())
                .body(mobilePushPayload.getBody())
                .token(mobilePushPayload.getToken())
                .build();
        mobilePushRepository.save(mobilePush);
        log.info("Mobile PushNotification created -> mobilePush: {}", mobilePush);
        try {
            asyncService.sendMobilePush(mobilePush);
        } catch (RejectedExecutionException ex) {
            waitAndResendAsync(mobilePush, 5, 1, TimeUnit.SECONDS);
        }
    }

    public void send(MobilePush mobilePush) {
        mobilePush.setStatus(NotificationStatus.PROCESSING);
        mobilePushRepository.save(mobilePush);
        log.info("Mobile PushNotification sending -> mobilePush: {}", mobilePush);
        mobilePush.setStatus(NotificationStatus.SENT);
        mobilePush.setSendAt(LocalDateTime.now());
        mobilePushRepository.save(mobilePush);
        log.info("Mobile PushNotification sent -> mobilePush: {}", mobilePush);
    }

    private void waitAndResendAsync(MobilePush mobilePush, int time, int retryCount, TimeUnit timeUnit) {
        if (retryCount > RETRY_LIMIT) {
            mobilePush.setStatus(NotificationStatus.FAILED);
            mobilePushRepository.save(mobilePush);
            log.error("Mobile PushNotification can not be send -> mobilePush: {}, retryCount: {}, time: {}, timeUnit:" +
                    " {}", mobilePush, retryCount, time, timeUnit);
            return;
        }
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        try {
            asyncService.sendMobilePush(mobilePush);
        } catch (RejectedExecutionException ex) {
            retryCount++;
            waitAndResendAsync(mobilePush, time, retryCount, timeUnit);
        }
    }

}
