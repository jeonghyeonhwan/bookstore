package com.example.demo.dto.order;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class OrderResponse {
    private final Long orderId;
    private final String status;
    private final BigDecimal totalPrice;
    private final String deliveryAddress;
    private final LocalDateTime createdAt;
    private final List<OrderItemDetail> items;

    @Getter
    @Builder
    public static class OrderItemDetail {
        private final Long bookId;
        private final String bookTitle;
        private final int quantity;
        private final BigDecimal unitPrice; // 개당 가격
        private final BigDecimal totalAmount; // 수량 * 가격
    }

    public static OrderResponse from(Order order) {
        List<OrderItemDetail> itemDetails = order.getOrderItems().stream()
                .map(item -> OrderItemDetail.builder()
                        .bookId(item.getBook().getBookId())
                        .bookTitle(item.getBook().getTitle())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getPrice())
                        .totalAmount(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus().name())
                .totalPrice(order.getTotalPrice())
                .deliveryAddress(order.getDeliveryAddress())
                .createdAt(order.getCreatedAt())
                .items(itemDetails)
                .build();
    }
}