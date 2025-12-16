package com.example.demo.controller;

import com.example.demo.dto.common.ApiResponse;
import com.example.demo.dto.wishlist.WishlistResponse;
import com.example.demo.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlists")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{bookId}")
    public ResponseEntity<ApiResponse<Void>> addWishlist(
            @RequestHeader("Authorization") String token,
            @PathVariable Long bookId) {

        Long userId = getUserIdFromToken(token);
        wishlistService.addWishlist(userId, bookId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("찜 목록에 추가했습니다.", null));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<ApiResponse<Void>> removeWishlist(
            @RequestHeader("Authorization") String token,
            @PathVariable Long bookId) {

        Long userId = getUserIdFromToken(token);
        wishlistService.removeWishlist(userId, bookId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("찜 목록에서 삭제했습니다.", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WishlistResponse>>> getWishlist(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long userId = getUserIdFromToken(token);
        List<WishlistResponse> response = wishlistService.getWishlist(userId, page, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("찜 목록을 조회했습니다.", response));
    }

    private Long getUserIdFromToken(String token) {
        return 1L;
    }
}