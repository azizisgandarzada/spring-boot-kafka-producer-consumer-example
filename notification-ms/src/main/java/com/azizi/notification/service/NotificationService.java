package com.azizi.notification.service;

import com.azizi.common.payload.NotificationPayload;

public interface NotificationService {

    void addQueue(NotificationPayload payload);

}
