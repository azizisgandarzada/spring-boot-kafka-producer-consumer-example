package com.azizi.notification.service.factory;

import com.azizi.common.enums.NotificationType;
import com.azizi.notification.service.EmailService;
import com.azizi.notification.service.MobilePushService;
import com.azizi.notification.service.NotificationService;
import com.azizi.notification.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceFactory {

    private final ApplicationContext applicationContext;

    public NotificationService getNotificationService(NotificationType notificationType) {
        switch (notificationType) {
            case EMAIL:
                return applicationContext.getBean(EmailService.class);
            case SMS:
                return applicationContext.getBean(SmsService.class);
            case MOBILE_PUSH:
                return applicationContext.getBean(MobilePushService.class);
            default:
                log.error("Wrong notification type {}", notificationType);
                return null;
        }
    }

}
