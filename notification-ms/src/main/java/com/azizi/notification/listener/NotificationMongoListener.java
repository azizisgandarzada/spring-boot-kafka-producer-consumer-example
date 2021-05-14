package com.azizi.notification.listener;

import com.azizi.notification.document.Notification;
import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class NotificationMongoListener extends AbstractMongoEventListener<Notification> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Notification> event) {
        Notification notification = event.getSource();
        setCreatedOrModifiedDate(notification);
    }

    private void setCreatedOrModifiedDate(Notification notification) {
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(LocalDateTime.now());
        } else {
            notification.setModifiedAt(LocalDateTime.now());
        }
    }

}

