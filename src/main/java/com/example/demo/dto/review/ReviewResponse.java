package com.example.demo.dto.review;

import com.example.demo.entity.Review;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponse {
    private final Long reviewId;
    private final Long bookId;
    private final String userName; // 작성자 이름
    private final int rating;
    private final String comment;
    private final LocalDateTime createdAt;

    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .bookId(review.getBook().getBookId())
                .userName(review.getUser().getName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}