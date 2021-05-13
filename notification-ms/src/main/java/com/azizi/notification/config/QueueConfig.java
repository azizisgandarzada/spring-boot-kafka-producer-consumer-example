package com.azizi.notification.config;

import com.azizi.notification.document.Email;
import com.azizi.notification.document.MobilePush;
import com.azizi.notification.document.Sms;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Bean
    public LinkedBlockingQueue<Email> emailLinkedBlockingQueue() {
        return new LinkedBlockingQueue<>(50);
    }

    @Bean
    public LinkedBlockingQueue<Sms> smsLinkedBlockingQueue() {
        return new LinkedBlockingQueue<>(50);
    }

    @Bean
    public LinkedBlockingQueue<MobilePush> mobilePushLinkedBlockingQueue() {
        return new LinkedBlockingQueue<>(50);
    }

}
