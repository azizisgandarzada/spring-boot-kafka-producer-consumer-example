package com.azizi.notification.service;

import com.azizi.notification.document.Email;
import com.azizi.notification.document.MobilePush;
import com.azizi.notification.document.Sms;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncService {

    private final NotificationService<Email> emailService;
    private final NotificationService<Sms> smsService;
    private final NotificationService<MobilePush> mobilePushService;

    @Async("emailExecutor")
    public void sendEmail(Email email) {
        emailService.send(email);
    }

    @Async("smsExecutor")
    public void sendSms(Sms sms) {
        smsService.send(sms);
    }

    @Async("mobilePushExecutor")
    public void sendMobilePush(MobilePush mobilePush) {
        mobilePushService.send(mobilePush);
    }


}
