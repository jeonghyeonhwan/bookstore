package com.example.demo.repository;

import com.example.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 사용자의 주문 목록 조회 (최신순 정렬)
    // N+1 문제 방지를 위해 fetch join 사용 권장
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems oi JOIN FETCH oi.book WHERE o.user.userId = :userId ORDER BY o.createdAt DESC")
    List<Order> findAllByUserId(@Param("userId") Long userId);
}