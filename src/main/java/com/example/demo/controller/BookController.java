package com.example.demo.controller;

import com.example.demo.dto.book.*;
import com.example.demo.dto.common.ApiResponse;
import com.example.demo.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateBookResponse>> createBook(
            @Valid @RequestBody CreateBookRequest request) {

        CreateBookResponse responsePayload = bookService.createBook(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "도서가 등록되었습니다.",
                        responsePayload
                ));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<ApiResponse<GetBookResponse>> getBook(@PathVariable Long bookId) {

        GetBookResponse responsePayload = bookService.getBook(bookId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "도서 조회 성공",
                        responsePayload
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<GetBookListResponse>> getBookList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String keyword) {

        GetBookListResponse responsePayload = bookService.getBookList(page, size, sort, keyword);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "도서 목록 조회에 성공했습니다.",
                        responsePayload
                ));
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<ApiResponse<UpdateBookResponse>> updateBook(
            @PathVariable Long bookId,
            @RequestBody UpdateBookRequest request) {

        UpdateBookResponse responsePayload = bookService.updateBook(bookId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "도서가 성공적으로 수정되었습니다.",
                        responsePayload
                ));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<ApiResponse<DeleteBookResponse>> deleteBook(@PathVariable Long bookId) {

        DeleteBookResponse responsePayload = bookService.deleteBook(bookId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT) // 204 No Content
                .body(ApiResponse.success(
                        "도서가 성공적으로 삭제되었습니다.",
                        responsePayload
                ));
    }
}