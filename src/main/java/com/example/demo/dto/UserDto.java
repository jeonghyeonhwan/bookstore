package com.example.demo.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class UserDto {

    @Getter @Setter
    public static class RegisterRequest {
        @NotBlank(message = "이메일은 필수입니다.")
        @Email
        private String email;

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        @NotBlank(message = "이름은 필수입니다.")
        private String name;

        @NotBlank(message = "생년월일은 필수입니다.")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "YYYY-MM-DD 형식이어야 합니다.")
        private String birthDate;

        private String gender;
        private String address;
        private String phoneNumber;
    }

    @Getter @Builder
    public static class Response<T> {
        private boolean isSuccess;
        private String message;
        private T payload;
    }

    @Getter @Builder
    public static class RegisterPayload {
        private Long userId;
        private LocalDateTime createdAt;
    }
}