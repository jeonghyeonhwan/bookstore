package com.example.demo.service;

import com.example.demo.dto.user.*;
import com.example.demo.entity.User;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public UserRegisterResponse registerUser(UserRegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "이미 등록된 이메일입니다.");
        }

        if (request.getPhoneNumber() != null && userRepository.findByPhonenumber(request.getPhoneNumber()).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "이미 등록된 연락처 번호입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = request.toEntity(encodedPassword);
        User savedUser = userRepository.save(newUser);

        return UserRegisterResponse.from(savedUser);
    }

    public LoginUserResponse loginUser(LoginUserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        if (user.getDeletedAt() != null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "탈퇴(비활성화)된 계정입니다.");
        }

        String token = jwtService.createToken(user.getUserId(), user.getRole());

        return LoginUserResponse.from(token, user);
    }

    @Transactional
    public UpdateUserResponse updateUser(Long authenticatedUserId, UpdateUserRequest request) {
        User user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        boolean hasUpdateData = (request.getPassword() != null && !request.getPassword().isBlank()) ||
                request.getAddress() != null ||
                request.getPhoneNumber() != null;

        if (!hasUpdateData) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "수정할 항목이 없습니다.");
        }

        String newHashedPassword = null;
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            newHashedPassword = passwordEncoder.encode(request.getPassword());
        }

        user.updateDetails(newHashedPassword, request.getAddress(), request.getPhoneNumber());

        return UpdateUserResponse.from(user);
    }

    @Transactional
    public SoftDeleteUserResponse softDeleteUser(Long authenticatedUserId, SoftDeleteUserRequest request) {
        User user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        if (user.getDeletedAt() != null) {
            throw new ApiException(HttpStatus.CONFLICT, "이미 삭제된 사용자입니다.");
        }

        user.softDelete();

        return SoftDeleteUserResponse.from(user);
    }

    @Transactional
    public HardDeleteUserResponse hardDeleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "삭제할 사용자를 찾을 수 없습니다."));

        userRepository.delete(user);

        return HardDeleteUserResponse.success();
    }
}