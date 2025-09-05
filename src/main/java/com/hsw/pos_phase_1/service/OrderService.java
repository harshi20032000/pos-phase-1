package com.hsw.pos_phase_1.service;

import com.hsw.pos_phase_1.entities.Order;
import com.hsw.pos_phase_1.enums.OrderStatus;
import com.hsw.pos_phase_1.entities.Product;
import com.hsw.pos_phase_1.repository.OrderRepository;
import com.hsw.pos_phase_1.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public Order createOrder(Order order) {
        // calculate total
        double total = order.getItems().stream().mapToDouble(eachItem -> {
            Product product = productRepository.findById(eachItem.getProduct().getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + eachItem.getProduct().getProductId()));

            eachItem.setOrder(order); // link order
            eachItem.setPrice(product.getPrice()); // snapshot price
            return product.getPrice() * eachItem.getQuantity();
        }).sum();

        order.setTotalAmount(total);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    public Order updateOrder(Long orderId, Order updatedOrder) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

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
        double total = existingOrder.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        existingOrder.setTotalAmount(total);
        existingOrder.setStatus(OrderStatus.valueOf("PENDING")); // stays pending until paid

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

}
