package com.example.demo.dto.review;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class CreateReviewRequest {

    @NotNull(message = "주문 상세 ID는 필수입니다.")
    private Long orderItemId;

    @Min(value = 0, message = "평점은 0점 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5점 이하이어야 합니다.")
    private int rating;

    @Size(max = 1000, message = "리뷰 내용은 1000자 이내여야 합니다.")
    private String comment;
}