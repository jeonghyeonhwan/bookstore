package com.example.demo.controller;

import com.example.demo.dto.common.ApiResponse;
import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateOrderRequest request) {

        Long userId = getUserIdFromToken(token);

        OrderResponse response = orderService.createOrder(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("주문이 성공적으로 생성되었습니다.", response));
    }

    // 내 주문 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders(
            @RequestHeader("Authorization") String token) {

        Long userId = getUserIdFromToken(token);

        List<OrderResponse> response = orderService.getMyOrders(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("주문 목록을 조회했습니다.", response));
    }

    // 주문 상세 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable Long orderId) {

        Long userId = getUserIdFromToken(token);

        OrderResponse response = orderService.getOrderDetail(userId, orderId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("주문 상세 정보를 조회했습니다.", response));
    }

    // 주문 취소
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @RequestHeader("Authorization") String token,
            @PathVariable Long orderId) {

        Long userId = getUserIdFromToken(token);

        OrderResponse response = orderService.cancelOrder(userId, orderId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("주문이 취소되었습니다.", response));
    }

    // Mock User ID Extraction
    private Long getUserIdFromToken(String token) {
        return 1L;
    }
}