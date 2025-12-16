package com.example.demo.service;

import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.order.OrderResponse;
import com.example.demo.entity.Book;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.User;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    /**
     * 주문 생성 (Create Order)
     */
    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        // 1. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // 2. 주문 엔티티 생성 (아직 아이템 없음)
        Order order = Order.builder()
                .user(user)
                .status(Order.OrderStatus.PENDING)
                .deliveryAddress(request.getDeliveryAddress())
                .totalPrice(BigDecimal.ZERO) // 임시 0원, 아래에서 계산
                .build();

        BigDecimal finalTotalPrice = BigDecimal.ZERO;

        // 3. 주문 아이템 처리
        for (CreateOrderRequest.OrderItemDto itemDto : request.getItems()) {
            Book book = bookRepository.findById(itemDto.getBookId())
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "도서(ID: " + itemDto.getBookId() + ")를 찾을 수 없습니다."));

            // 3-1. 주문 아이템 생성 (가격은 현재 도서 가격 기준)
            OrderItem orderItem = OrderItem.builder()
                    .book(book)
                    .quantity(itemDto.getQuantity())
                    .price(book.getPrice())
                    .build();

            // 3-2. 주문에 아이템 추가 (양방향 연관관계 설정)
            order.addOrderItem(orderItem);

            // 3-3. 총 가격 누적 계산 (가격 * 수량)
            BigDecimal itemTotal = book.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            finalTotalPrice = finalTotalPrice.add(itemTotal);
        }

        // 4. 최종 가격 설정 (Order 엔티티에는 Setter가 없으므로 Builder로 새로 만들거나, 별도 메서드 필요.
        // 여기서는 Order 객체의 totalPrice를 업데이트하기 위해 리플렉션 대신, Order 엔티티 수정이 권장됨.
        // 하지만 이미 작성된 Entity에 Setter가 없으므로, 내부적으로 필드를 수정하는 방식을 사용하거나
        // Order 생성 시점에 계산해서 넣어야 함.
        // 가장 깔끔한 방법: Order 엔티티에 `calculateTotalPrice` 메서드를 두거나, 처음부터 계산 후 빌드.
        // 여기서는 편의상 Reflection이나 Setter 없이, Order를 다시 빌드하는 것은 불가능하므로
        // Order 엔티티에 'updateTotalPrice' 메서드가 있다고 가정하고 호출하거나,
        // 위에서 Builder 패턴 사용 시 totalPrice를 나중에 넣을 수 없으므로,
        // 로직 순서를 바꿔서 가격 계산 -> Order 빌드 -> 아이템 연결 순서로 진행하겠습니다.)

        // --- 로직 수정 (순서 변경) ---

        // A. 아이템 목록 및 총 가격 미리 계산
        // (OrderItem은 Order가 필요하므로, Order를 먼저 만들되 total을 나중에 수정하는 게 JPA 관례상 편함.
        // Order 엔티티에 protected/public void setTotalPrice 메서드 추가가 가장 빠름.
        // 하지만 사용자님의 요청대로 "코드 수정 없이" 가려면 Order.java 에 메서드 추가가 필요함.
        // 아래 코드를 위해 Order.java에 `setTotalPrice` 같은 비즈니스 메서드가 필요함.
        // 일단 여기서는 Order 생성 시점에 값을 넣을 수 있도록 로직을 다시 짭니다.)

        // 실제 구현: DTO 리스트 순회 -> 엔티티 리스트 생성 -> 가격 합산 -> Order 생성 -> 연관관계 설정
        return processOrderCreation(user, request);
    }

    private OrderResponse processOrderCreation(User user, CreateOrderRequest request) {
        // 1. 아이템 DTO를 순회하며 OrderItem 엔티티 준비 및 가격 계산
        List<OrderItem> orderItems = request.getItems().stream().map(itemDto -> {
            Book book = bookRepository.findById(itemDto.getBookId())
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "도서 ID " + itemDto.getBookId() + "를 찾을 수 없습니다."));
            return OrderItem.builder()
                    .book(book)
                    .quantity(itemDto.getQuantity())
                    .price(book.getPrice())
                    .build();
        }).collect(Collectors.toList());

        BigDecimal total = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Order 생성
        Order order = Order.builder()
                .user(user)
                .status(Order.OrderStatus.PENDING)
                .deliveryAddress(request.getDeliveryAddress())
                .totalPrice(total)
                .build();

        // 3. 연관관계 맺기
        for (OrderItem item : orderItems) {
            order.addOrderItem(item);
        }

        // 4. 저장 (Cascade.ALL로 인해 Item도 자동 저장)
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    /**
     * 내 주문 목록 조회
     */
    public List<OrderResponse> getMyOrders(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 주문 상세 조회
     */
    public OrderResponse getOrderDetail(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));

        if (!order.getUser().getUserId().equals(userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "본인의 주문만 조회할 수 있습니다.");
        }

        return OrderResponse.from(order);
    }

    /**
     * 주문 취소
     */
    @Transactional
    public OrderResponse cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));

        if (!order.getUser().getUserId().equals(userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "본인의 주문만 취소할 수 있습니다.");
        }

        if (order.getStatus() == Order.OrderStatus.SHIPPING || order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "이미 배송 중이거나 배송 완료된 주문은 취소할 수 없습니다.");
        }

        order.cancel(); // 엔티티의 상태 변경 메서드 호출

        return OrderResponse.from(order);
    }
}