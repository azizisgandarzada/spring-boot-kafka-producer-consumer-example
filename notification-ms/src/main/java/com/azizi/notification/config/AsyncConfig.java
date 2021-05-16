package com.azizi.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean
    public ThreadPoolTaskExecutor emailExecutor() {
        return getThreadPoolTaskExecutor();
    }

    @Bean
    public ThreadPoolTaskExecutor smsExecutor() {
        return getThreadPoolTaskExecutor();
    }

    @Bean
    public ThreadPoolTaskExecutor mobilePushExecutor() {
        return getThreadPoolTaskExecutor();
    }

    private ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(50);
        threadPoolTaskExecutor.setMaxPoolSize(100);
        threadPoolTaskExecutor.setQueueCapacity(50);
        threadPoolTaskExecutor.afterPropertiesSet();
        return threadPoolTaskExecutor;
    }

}
