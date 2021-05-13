package com.azizi.common.payload;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MobilePushPayload {

    private String token;
    private String title;
    private String body;
    private Map<String, Object> data;

}
