package com.example.demo.dto.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ApiResponse<T> {
    private final boolean isSuccess;
    private final String message;
    private final T payload;

    // 성공 응답
    public static <T> ApiResponse<T> success(String message, T payload) {
        return ApiResponse.<T>builder()
                .isSuccess(true)
                .message(message)
                .payload(payload)
                .build();
    }

    // 실패 응답
    public static <T> ApiResponse<T> fail(String message) {
        return ApiResponse.<T>builder()
                .isSuccess(false)
                .message(message)
                .payload(null)
                .build();
    }
}