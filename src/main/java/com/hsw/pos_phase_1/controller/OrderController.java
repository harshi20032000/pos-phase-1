package com.hsw.pos_phase_1.controller;

import com.hsw.pos_phase_1.entities.Order;
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
    public ResponseEntity<BaseUIResponse<Order>> createOrder(@RequestBody Order order) {
        Order savedOrder = orderService.createOrder(order);

        BaseUIResponse<Order> response = new BaseUIResponse<>();
        response.setResponsePayload(savedOrder);
        response.setMessage("Order created successfully");
        response.setStatus("SUCCESS");
        response.setCode("ORDER_CREATED");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<BaseUIResponse<Order>> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId); // throws if not found

        BaseUIResponse<Order> response = new BaseUIResponse<>();
        response.setResponsePayload(order);
        response.setMessage("Order fetched successfully");
        response.setStatus("SUCCESS");
        response.setCode("ORDER_FETCHED");

        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<BaseUIResponse<List<Order>>> getAllOrders() {
        List<Order> allOrders = orderService.getAllOrders();

        BaseUIResponse<List<Order>> response = new BaseUIResponse<>();
        response.setResponsePayload(allOrders);
        response.setMessage("Orders fetched successfully");
        response.setStatus("SUCCESS");
        response.setCode("ORDERS_FETCHED");

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{orderId}")
    public ResponseEntity<BaseUIResponse<String>> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrderById(orderId); // throws if not found

        BaseUIResponse<String> response = new BaseUIResponse<>();
        response.setResponsePayload("Order with ID " + orderId + " deleted successfully.");
        response.setMessage("Order deleted successfully");
        response.setStatus("SUCCESS");
        response.setCode("ORDER_DELETED");

        return ResponseEntity.ok(response);
    }


    @PutMapping("/{orderId}")
    public ResponseEntity<BaseUIResponse<Order>> updateOrder(@PathVariable Long orderId, @RequestBody Order updatedOrder) {

        Order savedOrder = orderService.updateOrder(orderId, updatedOrder);

        BaseUIResponse<Order> response = new BaseUIResponse<>();
        response.setResponsePayload(savedOrder);
        response.setMessage("Order updated successfully");
        response.setStatus("SUCCESS");
        response.setCode("ORDER_UPDATED");

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{orderId}/completep")
    public ResponseEntity<BaseUIResponse<Order>> payForOrder(@PathVariable Long orderId) {
        BaseUIResponse<Order> response = new BaseUIResponse<>();
        response.setResponsePayload(orderService.markOrderAsPaid(orderId));
        response.setMessage("Order updated successfully");
        response.setStatus("SUCCESS");
        response.setCode("ORDER_UPDATED");
        return ResponseEntity.ok(response);
    }

}
