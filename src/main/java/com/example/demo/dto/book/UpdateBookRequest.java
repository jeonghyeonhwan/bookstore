package com.example.demo.dto.book;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class UpdateBookRequest {

    private String title;
    private String author;
    private String publisher;
    private String summary;
    private String isbn;
    private BigDecimal price;
    private String publicationDate; // String으로 받고 내부에서 LocalDate 변환
}