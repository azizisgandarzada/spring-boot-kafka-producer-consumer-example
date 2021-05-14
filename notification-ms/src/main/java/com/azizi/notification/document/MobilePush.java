package com.azizi.notification.document;

import com.azizi.notification.enums.NotificationStatus;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MobilePush extends Notification {

    private Integer userId;
    private NotificationStatus status;
    private String token;
    private String title;
    private String body;
    private Map<String, Object> data;

}
