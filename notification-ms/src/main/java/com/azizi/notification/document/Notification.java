package com.azizi.notification.document;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class Notification {

    @Id
    private String id;

    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private LocalDateTime sendAt;
    private LocalDateTime modifiedAt;

}
