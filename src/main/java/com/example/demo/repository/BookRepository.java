package com.example.demo.repository;

import com.example.demo.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    // 검색어(Keyword)가 있을 경우 제목 또는 저자에 포함된 도서를 검색 (페이징 지원)
    Page<Book> findByTitleContainingOrAuthorContaining(String title, String author, Pageable pageable);
}