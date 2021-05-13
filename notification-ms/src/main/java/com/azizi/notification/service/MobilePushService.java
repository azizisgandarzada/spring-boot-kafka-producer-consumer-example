package com.azizi.notification.service;

import com.azizi.common.payload.MobilePushPayload;
import com.azizi.common.payload.NotificationPayload;
import com.azizi.notification.document.MobilePush;
import com.azizi.notification.enums.NotificationStatus;
import com.azizi.notification.repository.MobilePushRepository;
import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class MobilePushService implements NotificationService {


    private final LinkedBlockingQueue<MobilePush> mobilePushLinkedBlockingQueue;
    private final MobilePushRepository mobilePushRepository;
    private final AsyncService asyncService;

    @Override
    public void addQueue(NotificationPayload payload) {
        MobilePushPayload mobilePushPayload = payload.getMobilePush();
        MobilePush mobilePush = MobilePush.builder()
                .userId(payload.getUserId())
                .status(NotificationStatus.IN_QUEUE)
                .title(mobilePushPayload.getTitle())
                .body(mobilePushPayload.getBody())
                .token(mobilePushPayload.getToken())
                .createdAt(LocalDateTime.now())
                .build();
        mobilePushRepository.save(mobilePush);
        try {
            mobilePushLinkedBlockingQueue.put(mobilePush);
            log.info("Mobile PushNotification put to queue {}", mobilePush);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void processMobilePush() {
        while (true) {
            if (mobilePushLinkedBlockingQueue.isEmpty()) {
                continue;
            }
            try {
                MobilePush mobilePush = mobilePushLinkedBlockingQueue.take();
                log.info("Mobile PushNotification taken from queue {}", mobilePush);
                mobilePushRepository.findById(mobilePush.getId()).ifPresent(mp -> {
                    mp.setStatus(NotificationStatus.PROCESSING);
                    mp.setProcessedAt(LocalDateTime.now());
                    mobilePushRepository.save(mp);
                    asyncService.sendMobilePush(mp);
                });
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void send(MobilePush mobilePush) {
        log.info("Mobile Push Notification sending... {}", mobilePush);
        mobilePushRepository.findById(mobilePush.getId()).ifPresent(mp -> {
            mp.setStatus(NotificationStatus.SENT);
            mp.setSendAt(LocalDateTime.now());
            mobilePushRepository.save(mp);
            log.info("Mobile Push Notification sent {}", mp);
        });
    }

}
