package com.example.demo.controller;

import com.example.demo.dto.common.ApiResponse;
import com.example.demo.dto.user.*;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserRegisterResponse>> registerUser(
            @Valid @RequestBody UserRegisterRequest request) {

        UserRegisterResponse responsePayload = userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "회원가입이 완료되었습니다.",
                        responsePayload
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginUserResponse>> loginUser(
            @Valid @RequestBody LoginUserRequest request) {

        LoginUserResponse responsePayload = userService.loginUser(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "로그인 성공",
                        responsePayload
                ));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<UpdateUserResponse>> updateUser(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UpdateUserRequest request) {

        Long authenticatedUserId = getUserIdFromToken(token);

        UpdateUserResponse responsePayload = userService.updateUser(authenticatedUserId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "회원정보가 수정되었습니다.",
                        responsePayload
                ));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<SoftDeleteUserResponse>> softDeleteUser(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody SoftDeleteUserRequest request) {

        Long authenticatedUserId = getUserIdFromToken(token);

        SoftDeleteUserResponse responsePayload = userService.softDeleteUser(authenticatedUserId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "회원이 삭제되었습니다.",
                        responsePayload
                ));
    }

    // 하드 딜리트는 일반적으로 관리자 권한이 필요하며, 특정 userId를 PathVariable로 받음
    @DeleteMapping("/{userId}/hard")
    public ResponseEntity<ApiResponse<HardDeleteUserResponse>> hardDeleteUser(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token) {


        HardDeleteUserResponse responsePayload = userService.hardDeleteUser(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "회원이 완전히 삭제되었습니다.",
                        responsePayload
                ));
    }

    private Long getUserIdFromToken(String token) {
        return 1L;
    }
}