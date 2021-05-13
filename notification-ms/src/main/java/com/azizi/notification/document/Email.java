package com.azizi.notification.document;

import com.azizi.notification.enums.NotificationStatus;
import java.time.LocalDateTime;
import java.util.List;
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
public class Email {

    @Id
    private String id;

    private Integer userId;
    private NotificationStatus status;
    private String subject;
    private String content;
    private List<String> receivers;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private LocalDateTime sendAt;

}
