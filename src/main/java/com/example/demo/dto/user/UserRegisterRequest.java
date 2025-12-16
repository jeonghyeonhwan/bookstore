package com.example.demo.dto.user;

import com.example.demo.entity.User;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.math.BigDecimal;

@Getter
@Setter
public class UserRegisterRequest {

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message = "생년월일은 필수 입력 항목입니다. (YYYY-MM-DD)")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일 형식(YYYY-MM-DD)이 올바르지 않습니다.")
    private String birthDate;

    private String gender; // 옵션
    private String address; // 옵션
    private String phoneNumber; // 옵션

    public User toEntity(String encodedPassword) {
        LocalDate date = LocalDate.parse(this.birthDate);

        return User.builder()
                .email(this.email)
                .password(encodedPassword)
                .name(this.name)
                .birthdate(date)
                .gender(this.gender)
                .phonenumber(this.phoneNumber)
                .address(this.address)
                .build();
    }
}