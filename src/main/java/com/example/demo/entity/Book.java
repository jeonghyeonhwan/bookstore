package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, unique = true, length = 13)
    private String isbn;

    @Column(nullable = false, length = 100)
    private String publisher;

    @Lob
    private String summary;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Book(String title, String author, String publisher, String summary, String isbn, BigDecimal price, LocalDate publicationDate) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.summary = summary;
        this.isbn = isbn;
        this.price = price;
        this.publicationDate = publicationDate;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateBookDetails(String title, String author, String publisher, String summary, String isbn, BigDecimal price, String publicationDateStr) {
        if (title != null) this.title = title;
        if (author != null) this.author = author;
        if (publisher != null) this.publisher = publisher;
        if (summary != null) this.summary = summary;
        if (isbn != null) this.isbn = isbn;
        if (price != null) this.price = price;
        if (publicationDateStr != null) {
            this.publicationDate = LocalDate.parse(publicationDateStr);
        }
        this.updatedAt = LocalDateTime.now();
    }
}