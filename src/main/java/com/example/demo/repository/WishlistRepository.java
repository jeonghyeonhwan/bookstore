package com.example.demo.repository;

import com.example.demo.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    // 이미 찜한 도서인지 확인
    boolean existsByUserUserIdAndBookBookId(Long userId, Long bookId);

    // 찜 삭제를 위해 조회
    Optional<Wishlist> findByUserUserIdAndBookBookId(Long userId, Long bookId);

    // 찜 목록 조회
    Page<Wishlist> findAllByUserUserId(Long userId, Pageable pageable);
}