package com.azizi.notification.service;

import com.azizi.common.payload.EmailPayload;
import com.azizi.common.payload.NotificationPayload;
import com.azizi.notification.document.Email;
import com.azizi.notification.enums.NotificationStatus;
import com.azizi.notification.repository.EmailRepository;
import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class EmailService implements NotificationService<Email> {

    private final LinkedBlockingQueue<Email> emailLinkedBlockingQueue;
    private final EmailRepository emailRepository;
    private final AsyncService asyncService;

    @Override
    public void addQueue(NotificationPayload payload) {
        EmailPayload emailPayload = payload.getEmail();
        Email email = Email.builder()
                .userId(payload.getUserId())
                .status(NotificationStatus.IN_QUEUE)
                .subject(emailPayload.getSubject())
                .content(emailPayload.getContent())
                .receivers(emailPayload.getReceivers())
                .build();
        emailRepository.save(email);
        try {
            emailLinkedBlockingQueue.put(email);
            log.info("Email put to queue {}", email);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void processNotification() {
        while (true) {
            if (emailLinkedBlockingQueue.isEmpty()) {
                continue;
            }
            try {
                Email notification = emailLinkedBlockingQueue.take();
                log.info("Email taken from queue {}", notification);
                emailRepository.findById(notification.getId()).ifPresent(email -> {
                    email.setStatus(NotificationStatus.PROCESSING);
                    email.setProcessedAt(LocalDateTime.now());
                    emailRepository.save(email);
                    asyncService.sendEmail(email);
                });
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void send(Email notification) {
        emailRepository.findById(notification.getId()).ifPresent(email -> {
            log.info("Email sending... {}", email);
            email.setStatus(NotificationStatus.SENT);
            email.setSendAt(LocalDateTime.now());
            emailRepository.save(email);
            log.info("Email sent {}", email);
        });
    }

}
