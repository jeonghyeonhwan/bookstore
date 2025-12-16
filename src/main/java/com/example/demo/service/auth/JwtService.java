package com.example.demo.service.auth;

import com.example.demo.entity.User;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    public String createToken(Long userId, User.Role role) {
        // 실제 JWT 생성 로직 대신 임시 토큰 반환
        return "MOCK_JWT_TOKEN.ID_" + userId + ".ROLE_" + role.name();
    }
}