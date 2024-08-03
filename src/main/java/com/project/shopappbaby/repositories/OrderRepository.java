package com.project.shopappbaby.repositories;

import com.project.shopappbaby.models.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository  extends JpaRepository<Order,Long> {
    // Tìm các dơn hàng của 1 user nào đó
    List<Order> findByUserId(Long userId);
}
