package com.example.demo.dto.user;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class SoftDeleteUserRequest {

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;
}