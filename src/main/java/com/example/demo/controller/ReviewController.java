package com.example.demo.controller;

import com.example.demo.dto.common.ApiResponse;
import com.example.demo.dto.review.CreateReviewRequest;
import com.example.demo.dto.review.ReviewResponse;
import com.example.demo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 생성
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateReviewRequest request) {

        Long userId = getUserIdFromToken(token);

        ReviewResponse response = reviewService.createReview(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("리뷰가 등록되었습니다.", response));
    }

    // 도서별 리뷰 목록 조회 (GET /api/v1/reviews?bookId=1)
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviews(
            @RequestParam Long bookId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<ReviewResponse> response = reviewService.getReviewsByBookId(bookId, page, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("리뷰 목록을 조회했습니다.", response));
    }

    private Long getUserIdFromToken(String token) {
        return 1L;
    }
}