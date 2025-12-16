package com.example.demo.dto.user;

import com.example.demo.entity.User;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class SoftDeleteUserResponse {
    private final LocalDateTime deletedAt;

    public static SoftDeleteUserResponse from(User user) {
        return SoftDeleteUserResponse.builder()
                .deletedAt(user.getDeletedAt())
                .build();
    }
}