package com.example.demo.controller;

import com.example.demo.dto.cart.AddCartItemRequest;
import com.example.demo.dto.cart.CartResponse;
import com.example.demo.dto.common.ApiResponse;
import com.example.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody AddCartItemRequest request) {

        Long userId = getUserIdFromToken(token);
        CartResponse response = cartService.addToCart(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("장바구니에 담았습니다.", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(
            @RequestHeader("Authorization") String token) {

        Long userId = getUserIdFromToken(token);
        CartResponse response = cartService.getCart(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("장바구니를 조회했습니다.", response));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> removeCartItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long cartItemId) {

        Long userId = getUserIdFromToken(token);
        cartService.removeCartItem(userId, cartItemId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("장바구니에서 상품을 삭제했습니다.", null));
    }

    private Long getUserIdFromToken(String token) {
        return 1L;
    }
}