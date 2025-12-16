package com.example.demo.dto.book;

import com.example.demo.entity.Book;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class UpdateBookResponse {
    private final Long bookId;
    private final LocalDateTime updatedAt;

    public static UpdateBookResponse from(Book book) {
        return UpdateBookResponse.builder()
                .bookId(book.getBookId())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}