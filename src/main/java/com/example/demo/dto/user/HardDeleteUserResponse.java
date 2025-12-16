package com.example.demo.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HardDeleteUserResponse {
    private final boolean success;

    public static HardDeleteUserResponse success() {
        return HardDeleteUserResponse.builder()
                .success(true)
                .build();
    }
}