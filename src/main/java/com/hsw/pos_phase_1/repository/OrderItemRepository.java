package com.hsw.pos_phase_1.repository;


import com.hsw.pos_phase_1.model.Order;
import com.hsw.pos_phase_1.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
}
