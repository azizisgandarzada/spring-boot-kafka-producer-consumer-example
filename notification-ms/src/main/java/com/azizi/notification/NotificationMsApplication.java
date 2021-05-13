package com.azizi.notification;

import com.azizi.notification.service.EmailService;
import com.azizi.notification.service.MobilePushService;
import com.azizi.notification.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@RequiredArgsConstructor
public class NotificationMsApplication implements CommandLineRunner {

    private final EmailService emailService;
    private final SmsService smsService;
    private final MobilePushService mobilePushService;

    public static void main(String[] args) {
        SpringApplication.run(NotificationMsApplication.class, args);
    }

    @Override
    public void run(String... args) {
        new Thread(emailService::processEmail).start();
        new Thread(smsService::processSms).start();
        new Thread(mobilePushService::processMobilePush).start();
    }

}
