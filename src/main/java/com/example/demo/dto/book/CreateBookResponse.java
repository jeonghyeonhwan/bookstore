package com.example.demo.dto.book;

import com.example.demo.entity.Book;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class CreateBookResponse {
    private final Long bookId; // 등록된 도서의 ID
    private final LocalDateTime createdAt; // 도서 등록 시간

    public static CreateBookResponse from(Book book) {
        return CreateBookResponse.builder()
                .bookId(book.getBookId())
                .createdAt(book.getCreatedAt())
                .build();
    }
}