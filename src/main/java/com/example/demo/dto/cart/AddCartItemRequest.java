package com.example.demo.dto.cart;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class AddCartItemRequest {

    @NotNull(message = "도서 ID는 필수입니다.")
    private Long bookId;

    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    private int quantity;
}