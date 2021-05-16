package com.azizi.notification.service;

import com.azizi.common.payload.EmailPayload;
import com.azizi.common.payload.NotificationPayload;
import com.azizi.notification.document.Email;
import com.azizi.notification.enums.NotificationStatus;
import com.azizi.notification.repository.EmailRepository;
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
public class EmailService implements NotificationService<Email> {

    private static final Integer RETRY_LIMIT = 5;

    private final EmailRepository emailRepository;
    private final AsyncService asyncService;

    @Override
    public void createAndSend(NotificationPayload payload) {
        EmailPayload emailPayload = payload.getEmail();
        Email email = Email.builder()
                .userId(payload.getUserId())
                .status(NotificationStatus.CREATED)
                .subject(emailPayload.getSubject())
                .content(emailPayload.getContent())
                .receivers(emailPayload.getReceivers())
                .build();
        emailRepository.save(email);
        log.info("Email created -> email: {}", email);
        try {
            asyncService.sendEmail(email);
        } catch (RejectedExecutionException ex) {
            waitAndResendAsync(email, 5, 1, TimeUnit.SECONDS);
        }
    }

    @Override
    public void send(Email email) {
        email.setStatus(NotificationStatus.PROCESSING);
        emailRepository.save(email);
        log.info("Email sending -> email: {}", email);
        email.setStatus(NotificationStatus.SENT);
        email.setSendAt(LocalDateTime.now());
        emailRepository.save(email);
        log.info("Email sent -> email: {}", email);
    }

    private void waitAndResendAsync(Email email, int time, int retryCount, TimeUnit timeUnit) {
        if (retryCount > RETRY_LIMIT) {
            email.setStatus(NotificationStatus.FAILED);
            emailRepository.save(email);
            log.error("Email can not be send -> email: {}, retryCount: {}, time: {}, timeUnit: {}", email, retryCount,
                    time, timeUnit);
            return;
        }
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        try {
            asyncService.sendEmail(email);
        } catch (RejectedExecutionException ex) {
            retryCount++;
            waitAndResendAsync(email, time, retryCount, timeUnit);
        }
    }

}
