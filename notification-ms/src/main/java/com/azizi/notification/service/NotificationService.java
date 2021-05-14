package com.azizi.notification.service;

import com.azizi.common.payload.NotificationPayload;

public interface NotificationService<T> {

    void addQueue(NotificationPayload payload);

    void processNotification();

    void send(T notification);

}
