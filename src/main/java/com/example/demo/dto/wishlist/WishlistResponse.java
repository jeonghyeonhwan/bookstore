package com.example.demo.dto.wishlist;

import com.example.demo.entity.Wishlist;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class WishlistResponse {
    private final Long wishlistId;
    private final Long bookId;
    private final String title;
    private final String author;
    private final LocalDateTime createdAt;

    public static WishlistResponse from(Wishlist wishlist) {
        return WishlistResponse.builder()
                .wishlistId(wishlist.getWishlistId())
                .bookId(wishlist.getBook().getBookId())
                .title(wishlist.getBook().getTitle())
                .author(wishlist.getBook().getAuthor())
                .createdAt(wishlist.getCreatedAt())
                .build();
    }
}