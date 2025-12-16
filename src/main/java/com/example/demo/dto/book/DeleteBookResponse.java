package com.example.demo.dto.book;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteBookResponse {
    private final boolean success;

    public static DeleteBookResponse success() {
        return DeleteBookResponse.builder()
                .success(true)
                .build();
    }
}