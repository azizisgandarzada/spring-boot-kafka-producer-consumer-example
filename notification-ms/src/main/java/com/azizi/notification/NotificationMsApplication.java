package com.azizi.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NotificationMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationMsApplication.class, args);
    }

}
