package com.example.demo.service;

import com.example.demo.dto.cart.AddCartItemRequest;
import com.example.demo.dto.cart.CartResponse;
import com.example.demo.entity.Book;
import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.User;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    /**
     * 장바구니 담기 (Add to Cart)
     */
    @Transactional
    public CartResponse addToCart(Long userId, AddCartItemRequest request) {
        // 1. 사용자 및 도서 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "도서를 찾을 수 없습니다."));

        // 2. 사용자의 장바구니 조회 (없으면 생성)
        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().user(user).build();
                    return cartRepository.save(newCart);
                });

        // 3. 이미 담겨있는 도서인지 확인
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndBook(cart, book);

        if (existingItem.isPresent()) {
            // 이미 있으면 수량 증가
            existingItem.get().addQuantity(request.getQuantity());
        } else {
            // 없으면 새로 추가
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .book(book)
                    .quantity(request.getQuantity())
                    .build();
            cartItemRepository.save(newItem);
            // Cart 엔티티 리스트 갱신을 위해 추가 (JPA 영속성 컨텍스트 고려)
            cart.getCartItems().add(newItem);
        }

        return CartResponse.from(cart);
    }

    /**
     * 장바구니 조회
     */
    public CartResponse getCart(Long userId) {
        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "장바구니가 비어있거나 존재하지 않습니다."));

        return CartResponse.from(cart);
    }

    /**
     * 장바구니 아이템 삭제
     */
    @Transactional
    public void removeCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "장바구니 항목을 찾을 수 없습니다."));

        if (!cartItem.getCart().getUser().getUserId().equals(userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "본인의 장바구니 항목만 삭제할 수 있습니다.");
        }

        cartItemRepository.delete(cartItem);
    }
}