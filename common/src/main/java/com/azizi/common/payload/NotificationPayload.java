package com.azizi.common.payload;

import com.azizi.common.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPayload {

    private Integer userId;
    private NotificationType type;
    private EmailPayload email;
    private SmsPayload sms;
    private MobilePushPayload mobilePush;

}
