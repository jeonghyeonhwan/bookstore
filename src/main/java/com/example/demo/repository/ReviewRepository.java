package com.example.demo.repository;

import com.example.demo.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 특정 도서의 리뷰 목록 조회 (페이징)
    Page<Review> findByBookBookId(Long bookId, Pageable pageable);

    // 해당 주문 항목에 대한 리뷰가 이미 존재하는지 확인
    boolean existsByOrderItemOrderItemId(Long orderItemId);
}