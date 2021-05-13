package com.azizi.notification.config;

import com.azizi.common.constants.KafkaGroupConstants;
import com.azizi.common.payload.NotificationPayload;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaConsumerConfig {

    @Value("${kafka.bootstrap-servers}")
    String bootstrapServers;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NotificationPayload> notificationListenerContainerFactory() {
        return listenerContainerFactory(KafkaGroupConstants.NOTIFICATION_SENDER);
    }

    private ConsumerFactory<String, NotificationPayload> consumerFactory(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15 * 1000);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
                new JsonDeserializer<>(NotificationPayload.class));
    }

    private ConcurrentKafkaListenerContainerFactory<String, NotificationPayload> listenerContainerFactory(String groupId) {
        ConcurrentKafkaListenerContainerFactory<String, NotificationPayload> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(groupId));
        return factory;
    }

}
