package com.hsw.pos_phase_1.repository;

import com.hsw.pos_phase_1.entities.Order;
import com.hsw.pos_phase_1.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
}