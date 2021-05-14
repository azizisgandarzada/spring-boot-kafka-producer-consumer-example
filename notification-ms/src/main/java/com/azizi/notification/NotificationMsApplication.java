package com.azizi.notification;

import com.azizi.notification.document.Email;
import com.azizi.notification.document.MobilePush;
import com.azizi.notification.document.Sms;
import com.azizi.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@RequiredArgsConstructor
public class NotificationMsApplication implements CommandLineRunner {

    private final NotificationService<Email> emailService;
    private final NotificationService<Sms> smsService;
    private final NotificationService<MobilePush> mobilePushService;

    public static void main(String[] args) {
        SpringApplication.run(NotificationMsApplication.class, args);
    }

    @Override
    public void run(String... args) {
        new Thread(emailService::processNotification).start();
        new Thread(smsService::processNotification).start();
        new Thread(mobilePushService::processNotification).start();
    }

}
