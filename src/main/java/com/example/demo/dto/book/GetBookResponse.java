package com.example.demo.dto.book;

import com.example.demo.entity.Book;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class GetBookResponse {
    private final Long bookId;
    private final String title;
    private final String author;
    private final String publisher;
    private final String summary;
    private final String isbn;
    private final BigDecimal price;
    private final LocalDate publicationDate;
    private final String createdAt;

    public static GetBookResponse from(Book book) {
        return GetBookResponse.builder()
                .bookId(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .summary(book.getSummary())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .publicationDate(book.getPublicationDate())
                .createdAt(book.getCreatedAt().toString())
                .build();
    }
}