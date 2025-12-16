package com.example.demo.service;

import com.example.demo.dto.book.*;
import com.example.demo.entity.Book;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public CreateBookResponse createBook(CreateBookRequest request) {
        if (bookRepository.findByIsbn(request.getIsbn()).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "이미 등록된 ISBN입니다.");
        }

        Book newBook = request.toEntity();
        Book savedBook = bookRepository.save(newBook);

        return CreateBookResponse.from(savedBook);
    }

    public GetBookResponse getBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "해당 ID의 도서를 찾을 수 없습니다."));

        return GetBookResponse.from(book);
    }

    @Transactional
    public UpdateBookResponse updateBook(Long bookId, UpdateBookRequest request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "수정 대상 도서를 찾을 수 없습니다."));

        boolean hasUpdateData = request.getTitle() != null || request.getAuthor() != null ||
                request.getPublisher() != null || request.getSummary() != null ||
                request.getIsbn() != null || request.getPrice() != null ||
                request.getPublicationDate() != null;

        if (!hasUpdateData) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "수정할 항목이 없습니다.");
        }

        book.updateBookDetails(
                request.getTitle(),
                request.getAuthor(),
                request.getPublisher(),
                request.getSummary(),
                request.getIsbn(),
                request.getPrice(),
                request.getPublicationDate()
        );

        return UpdateBookResponse.from(book);
    }

    public GetBookListResponse getBookList(int page, int size, String sort, String keyword) {
        // Pageable 생성 (1-based page index 보정을 위해 page - 1 처리 필요하지만, 요청이 0부터 온다면 그대로 사용)
        // 여기서는 편의상 요청이 1부터 온다고 가정하고 -1 처리하거나, 0부터 온다고 가정함.
        // Spring Data JPA의 PageRequest는 0부터 시작합니다.
        int pageIndex = (page > 0) ? page - 1 : 0;

        Sort sortObj = Sort.by(Sort.Direction.ASC, "title"); // 기본 정렬
        if ("latest".equals(sort)) {
            sortObj = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        Pageable pageable = PageRequest.of(pageIndex, size, sortObj);
        Page<Book> bookPage;

        if (keyword != null && !keyword.isBlank()) {
            bookPage = bookRepository.findByTitleContainingOrAuthorContaining(keyword, keyword, pageable);
        } else {
            bookPage = bookRepository.findAll(pageable);
        }

        if (bookPage.isEmpty()) {
            // 빈 페이지라도 에러보다는 빈 리스트 반환이 일반적이나 명세에 따라 예외 처리 가능
            // 여기서는 정상적으로 빈 리스트 반환
        }

        return GetBookListResponse.from(bookPage);
    }

    @Transactional
    public DeleteBookResponse deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "삭제할 도서를 찾을 수 없습니다."));

        bookRepository.delete(book);

        return DeleteBookResponse.success();
    }
}