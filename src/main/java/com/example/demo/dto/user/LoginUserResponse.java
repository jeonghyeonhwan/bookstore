package com.example.demo.dto.user;

import com.example.demo.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginUserResponse {
    private final String token; // JWT 인증토큰
    private final UserInfo user;

    @Getter
    @Builder
    public static class UserInfo {
        private final Long id; // 사용자 ID
        private final String name; // 로그인된 사용자 이름
        private final String role; // 사용자 권한
    }

    public static LoginUserResponse from(String token, User user) {
        return LoginUserResponse.builder()
                .token(token)
                .user(UserInfo.builder()
                        .id(user.getUserId())
                        .name(user.getName())
                        .role(user.getRole().name())
                        .build())
                .build();
    }
}