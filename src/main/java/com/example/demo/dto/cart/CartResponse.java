package com.example.demo.dto.cart;

import com.example.demo.entity.Cart;
import lombok.Builder;
import lombok.Getter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CartResponse {
    private final Long cartId;
    private final List<CartItemDetail> items;

    @Getter
    @Builder
    public static class CartItemDetail {
        private final Long cartItemId;
        private final Long bookId;
        private final String title;
        private final String author;
        private final int quantity;
    }

    public static CartResponse from(Cart cart) {
        List<CartItemDetail> details = cart.getCartItems().stream()
                .map(item -> CartItemDetail.builder()
                        .cartItemId(item.getCartItemId())
                        .bookId(item.getBook().getBookId())
                        .title(item.getBook().getTitle())
                        .author(item.getBook().getAuthor())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return CartResponse.builder()
                .cartId(cart.getCartId())
                .items(details)
                .build();
    }
}