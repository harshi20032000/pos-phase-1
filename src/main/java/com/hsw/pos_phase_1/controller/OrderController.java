package com.hsw.pos_phase_1.controller;

import com.hsw.pos_phase_1.entities.Order;
import com.hsw.pos_phase_1.entities.Product;
import com.hsw.pos_phase_1.models.BaseUIResponse;
import com.hsw.pos_phase_1.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public BaseUIResponse<Order> createOrder(@RequestBody Order order) {
        Order savedOrder = orderService.createOrder(order);
        BaseUIResponse<Order> baseUIResponse = new BaseUIResponse<>();
        baseUIResponse.setResponsePayload(savedOrder);
        return baseUIResponse;
    }

    @GetMapping("/{orderId}")
    public BaseUIResponse<Order> getOrderById(@PathVariable Long orderId) {
        BaseUIResponse<Order> baseUIResponse = new BaseUIResponse<>();
        baseUIResponse.setResponsePayload(orderService.getOrderById(orderId));
        return baseUIResponse;
    }

    @GetMapping
    public BaseUIResponse<List<Order>> getAllOrders() {
        BaseUIResponse<List<Order>>  baseUIResponse = new BaseUIResponse<>();
        baseUIResponse.setResponsePayload(orderService.getAllOrders());
        return baseUIResponse;
    }

    @DeleteMapping("/{orderId}")
    public BaseUIResponse<String> deleteOrder(@PathVariable Long orderId) {
        boolean isDeleted = orderService.deleteOrderById(orderId);
        BaseUIResponse<String> response = new BaseUIResponse<>();

        if (isDeleted) {
            response.setResponsePayload("Order with ID " + orderId + " deleted successfully.");
        } else {
            response.setResponsePayload("Failed to delete Order with ID " + orderId + ".");
        }

        return response;
    }

    @PutMapping("/{orderId}")
    public BaseUIResponse<Order> updateOrder(@PathVariable Long orderId,
                                             @RequestBody Order updatedOrder) {
        BaseUIResponse<Order> baseUIResponse = new BaseUIResponse<>();
        baseUIResponse.setResponsePayload(orderService.updateOrder(orderId, updatedOrder));
        return baseUIResponse;
    }

    @PatchMapping("/{orderId}/completep")
    public BaseUIResponse<Order> payForOrder(@PathVariable Long orderId) {
        BaseUIResponse<Order> baseUIResponse = new BaseUIResponse<>();
        baseUIResponse.setResponsePayload(orderService.markOrderAsPaid(orderId));
        return baseUIResponse;
    }

}
