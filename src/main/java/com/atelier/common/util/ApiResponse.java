package com.atelier.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
    private int responseCode;
    private String responseTime;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ApiResponse(int responseCode, String message, T data) {
        this.responseCode = responseCode;
        this.responseTime = Instant.now().toString();
        this.message = message;
        this.data = data;
    }

}
