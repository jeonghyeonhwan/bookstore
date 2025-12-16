package com.example.demo.dto.user;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
public class UpdateUserRequest {

    private String password;

    private String address;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식(010-XXXX-XXXX)이 올바르지 않습니다.")
    private String phoneNumber;
}