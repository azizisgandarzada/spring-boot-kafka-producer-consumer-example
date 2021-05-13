package com.azizi.notification.document;

import com.azizi.notification.enums.NotificationStatus;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MobilePush {

    @Id
    private String id;

    private Integer userId;
    private NotificationStatus status;
    private String token;
    private String title;
    private String body;
    private Map<String, Object> data;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private LocalDateTime sendAt;

}
