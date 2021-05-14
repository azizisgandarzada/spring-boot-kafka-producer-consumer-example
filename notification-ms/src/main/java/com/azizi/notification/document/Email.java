package com.azizi.notification.document;

import com.azizi.notification.enums.NotificationStatus;
import java.util.List;
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
public class Email extends Notification {

    private Integer userId;
    private NotificationStatus status;
    private String subject;
    private String content;
    private List<String> receivers;

}
