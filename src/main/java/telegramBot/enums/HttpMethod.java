package telegramBot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpMethod {
    GET("GET"),
    POST("POST");

    private final String methodName;
}
