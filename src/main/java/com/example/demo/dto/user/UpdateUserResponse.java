package com.example.demo.dto.user;

import com.example.demo.entity.User;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class UpdateUserResponse {
    private final Long userId;
    private final LocalDateTime updatedAt;

    public static UpdateUserResponse from(User user) {
        return UpdateUserResponse.builder()
                .userId(user.getUserId())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}