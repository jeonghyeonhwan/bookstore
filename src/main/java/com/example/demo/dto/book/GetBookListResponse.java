package com.example.demo.dto.book;

import com.example.demo.entity.Book;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetBookListResponse {
    private final List<BookSummary> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;

    @Getter
    @Builder
    public static class BookSummary {
        private final Long bookId;
        private final String title;
        private final String author;
        private final String publisher;
        private final BigDecimal price;
    }

    public static GetBookListResponse from(Page<Book> bookPage) {
        List<BookSummary> content = bookPage.getContent().stream()
                .map(book -> BookSummary.builder()
                        .bookId(book.getBookId())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .publisher(book.getPublisher())
                        .price(book.getPrice())
                        .build())
                .collect(Collectors.toList());

        return GetBookListResponse.builder()
                .content(content)
                .page(bookPage.getNumber() + 1) // 0-based index 보정
                .size(bookPage.getSize())
                .totalElements(bookPage.getTotalElements())
                .totalPages(bookPage.getTotalPages())
                .build();
    }
}