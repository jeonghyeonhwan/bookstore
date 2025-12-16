package com.example.demo.service;

import com.example.demo.dto.review.CreateReviewRequest;
import com.example.demo.dto.review.ReviewResponse;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    /**
     * 리뷰 생성
     */
    @Transactional
    public ReviewResponse createReview(Long userId, CreateReviewRequest request) {
        // 1. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // 2. 주문 상세(OrderItem) 조회
        // OrderItemRepository가 필요합니다. (아래 5번 항목에서 추가 예정)
        OrderItem orderItem = orderItemRepository.findById(request.getOrderItemId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."));

        // 3. 권한 검증 (본인이 주문한 항목인지)
        if (!orderItem.getOrder().getUser().getUserId().equals(userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "본인이 구매한 도서에 대해서만 리뷰를 작성할 수 있습니다.");
        }

        // 4. 중복 리뷰 검증
        if (reviewRepository.existsByOrderItemOrderItemId(request.getOrderItemId())) {
            throw new ApiException(HttpStatus.CONFLICT, "이미 리뷰를 작성한 주문 항목입니다.");
        }

        // 5. 리뷰 엔티티 생성 및 저장
        Review review = Review.builder()
                .user(user)
                .book(orderItem.getBook()) // OrderItem을 통해 Book 정보 가져옴
                .orderItem(orderItem)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);

        return ReviewResponse.from(savedReview);
    }

    /**
     * 특정 도서의 리뷰 목록 조회
     */
    public List<ReviewResponse> getReviewsByBookId(Long bookId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Review> reviewPage = reviewRepository.findByBookBookId(bookId, pageable);

        return reviewPage.getContent().stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
    }
}