package com.example.demo.service;

import com.example.demo.dto.wishlist.WishlistResponse;
import com.example.demo.entity.Book;
import com.example.demo.entity.User;
import com.example.demo.entity.Wishlist;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WishlistRepository;
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
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    /**
     * 찜 추가 (Toggle 방식이 아닌 명시적 추가/삭제로 구현)
     */
    @Transactional
    public void addWishlist(Long userId, Long bookId) {
        if (wishlistRepository.existsByUserUserIdAndBookBookId(userId, bookId)) {
            throw new ApiException(HttpStatus.CONFLICT, "이미 찜한 도서입니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "도서를 찾을 수 없습니다."));

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .book(book)
                .build();

        wishlistRepository.save(wishlist);
    }

    /**
     * 찜 삭제
     */
    @Transactional
    public void removeWishlist(Long userId, Long bookId) {
        Wishlist wishlist = wishlistRepository.findByUserUserIdAndBookBookId(userId, bookId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "찜 내역이 존재하지 않습니다."));

        wishlistRepository.delete(wishlist);
    }

    /**
     * 찜 목록 조회
     */
    public List<WishlistResponse> getWishlist(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Wishlist> wishlistPage = wishlistRepository.findAllByUserUserId(userId, pageable);

        return wishlistPage.getContent().stream()
                .map(WishlistResponse::from)
                .collect(Collectors.toList());
    }
}