package com.example.demo.exception;

import com.example.demo.dto.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
// MethodArgumentNotValidException은 Spring 프레임워크에 속하므로 그대로 유지합니다.

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 사용자 정의 예외 처리 (ApiException)
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ApiResponse.fail(e.getMessage()));
    }

    // Bean Validation 실패 시 처리 (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String defaultMessage = "요청 형식이 올바르지 않습니다.";
        if (e.getBindingResult().getFieldError() != null) {
            defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400
                .body(ApiResponse.fail(defaultMessage));
    }
}