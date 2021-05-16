package com.azizi.notification.service;

import com.azizi.common.payload.NotificationPayload;

public interface NotificationService<T> {

    void createAndSend(NotificationPayload payload);

    void send(T notification);

}
