package com.azizi.payment;

import com.azizi.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@RequiredArgsConstructor
@EnableAsync
public class PaymentMsApplication implements CommandLineRunner {

    private final PaymentService paymentService;

    public static void main(String[] args) {
        SpringApplication.run(PaymentMsApplication.class, args);
    }

    @Override
    public void run(String... args) {
        paymentService.sendEmailWhenPaymentIsSuccessful();
        paymentService.sendMobilePushWhenPaymentIsSuccessful();
        paymentService.sendSmsWhenPaymentIsSuccessful();
    }

}
