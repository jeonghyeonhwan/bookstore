package com.example.demo.dto.order;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {

    @NotEmpty(message = "주문할 도서 목록은 비어있을 수 없습니다.")
    @Valid // 리스트 내부 객체도 검증
    private List<OrderItemDto> items;

    @NotBlank(message = "배송 주소는 필수 입력 항목입니다.")
    private String deliveryAddress;

    @Getter
    @Setter
    public static class OrderItemDto {
        @NotNull(message = "도서 ID는 필수입니다.")
        private Long bookId;

        @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
        private int quantity;
    }
}