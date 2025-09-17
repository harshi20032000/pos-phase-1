package com.hsw.pos_phase_1.service;

import com.hsw.pos_phase_1.entities.Order;
import com.hsw.pos_phase_1.entities.Product;
import com.hsw.pos_phase_1.entities.User;
import com.hsw.pos_phase_1.enums.OrderStatus;
import com.hsw.pos_phase_1.repository.OrderRepository;
import com.hsw.pos_phase_1.repository.ProductRepository;
import com.hsw.pos_phase_1.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<Order> getAllOrders(Long userId) {
        //Admin user
        if (userId == 1)
            return orderRepository.findAll();
        return userRepository.findById(userId)
                .map(orderRepository::findAllByUser)
                .orElse(Collections.emptyList());
    }

    public Order getOrderById(Long id, Long userId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // If user is not admin, ensure they own the order
        if (userId != 1 && !order.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Access denied: You cannot view this order");
        }

        return order;
    }


    public Order createOrder(Order order) {
        //Assuming this user is present in DB
        if (order.getUser() == null) {
            User anonymousUser = userRepository.findByUsername("anonymous")
                    .orElseThrow(() -> new RuntimeException("Anonymous user not found"));
            order.setUser(anonymousUser);
        }

        order.setTotalAmount(calculateOrderTotal(order));
        return orderRepository.save(order);
    }

    public boolean deleteOrderById(Long id, Long userId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Only admin or the order creator can delete
        if (userId != 1 && !order.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Access denied: You cannot delete this order");
        }

        orderRepository.delete(order);
        return true;
    }


    public Order updateOrder(Long orderId, Order updatedOrder, Long userId) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        // Ownership check
        if (userId != 1 && !existingOrder.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Access denied: You cannot update this order");
        }

        // Status restrictions
        if ("PAID".equalsIgnoreCase(String.valueOf(existingOrder.getStatus()))) {
            throw new RuntimeException("Cannot update a paid order");
        }
        if ("COMPLETED".equalsIgnoreCase(String.valueOf(existingOrder.getStatus()))) {
            throw new RuntimeException("Cannot update a completed order");
        }

        // Clear old items and replace with new ones
        existingOrder.getItems().clear();
        updatedOrder.getItems().forEach(item -> {
            Product product = productRepository.findById(item.getProduct().getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProduct().getProductId()));

            item.setOrder(existingOrder);
            item.setPrice(product.getPrice()); // snapshot
            existingOrder.getItems().add(item);
        });

        // Recalculate total
        existingOrder.setTotalAmount(calculateOrderTotal(existingOrder));
        existingOrder.setStatus(OrderStatus.PENDING); // stays pending until paid

        return orderRepository.save(existingOrder);
    }


    public Order markOrderAsPaid(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        if ("PAID".equalsIgnoreCase(String.valueOf(order.getStatus()))) {
            throw new RuntimeException("Order already paid, proceed with new order");
        }

        if ("COMPLETED".equalsIgnoreCase(String.valueOf(order.getStatus()))) {
            throw new RuntimeException("Order already completed");
        }

        if (order.getItems().isEmpty()) {
            throw new RuntimeException("Cannot complete an empty order");
        }

        order.setStatus(OrderStatus.valueOf("PAID"));
        return orderRepository.save(order);
    }

    private double calculateOrderTotal(Order order) {
        return order.getItems().stream().mapToDouble(eachItem -> {
            Product product = productRepository.findById(eachItem.getProduct().getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + eachItem.getProduct().getProductId()));

            eachItem.setOrder(order);
            eachItem.setPrice(product.getPrice()); // snapshot price
            return product.getPrice() * eachItem.getQuantity();
        }).sum();
    }


}
