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
public class EmailService implements NotificationService {

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
                .createdAt(LocalDateTime.now())
                .build();
        emailRepository.save(email);
        try {
            emailLinkedBlockingQueue.put(email);
            log.info("Email put to queue {}", email);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void processEmail() {
        while (true) {
            if (emailLinkedBlockingQueue.isEmpty()) {
                continue;
            }
            try {
                Email email = emailLinkedBlockingQueue.take();
                log.info("Email taken from queue {}", email);
                emailRepository.findById(email.getId()).ifPresent(e -> {
                    e.setStatus(NotificationStatus.PROCESSING);
                    e.setProcessedAt(LocalDateTime.now());
                    emailRepository.save(e);
                    asyncService.sendEmail(e);
                });
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void send(Email email) {
        log.info("Email sending... {}", email);
        emailRepository.findById(email.getId()).ifPresent(e -> {
            e.setStatus(NotificationStatus.SENT);
            e.setSendAt(LocalDateTime.now());
            emailRepository.save(e);
            log.info("Email sent {}", e);
        });
    }

}
