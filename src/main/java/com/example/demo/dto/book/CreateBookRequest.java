package com.example.demo.dto.book;

import com.example.demo.entity.Book;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CreateBookRequest {

    @NotBlank(message = "도서명은 필수 입력 항목입니다.")
    private String title;

    @NotBlank(message = "저자는 필수 입력 항목입니다.")
    private String author;

    @NotBlank(message = "출판사는 필수 입력 항목입니다.")
    private String publisher;

    private String summary; // 옵션

    @NotBlank(message = "ISBN은 필수 입력 항목입니다.")
    private String isbn;

    @NotNull(message = "가격은 필수 입력 항목입니다.")
    @PositiveOrZero(message = "가격은 0 이상이어야 합니다.")
    private BigDecimal price;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "출판일 형식(YYYY-MM-DD)이 올바르지 않습니다.")
    private String publicationDate; // 옵션 (YYYY-MM-DD)

    public Book toEntity() {
        LocalDate pubDate = null;
        if (this.publicationDate != null && !this.publicationDate.isBlank()) {
            pubDate = LocalDate.parse(this.publicationDate);
        }

        return Book.builder()
                .title(this.title)
                .author(this.author)
                .publisher(this.publisher)
                .summary(this.summary)
                .isbn(this.isbn)
                .price(this.price)
                .publicationDate(pubDate)
                .build();
    }
}