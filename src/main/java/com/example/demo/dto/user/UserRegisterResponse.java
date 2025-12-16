package com.example.demo.dto.user;

import com.example.demo.entity.User;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class UserRegisterResponse {
    private final Long userId; // 생성된 사용자 고유 ID [cite: 56]
    private final LocalDateTime createdAt; // 사용자 생성 시간 [cite: 56]

    public static UserRegisterResponse from(User user) {
        return UserRegisterResponse.builder()
                .userId(user.getUserId())
                .createdAt(user.getCreatedAt())
                .build();
    }
}