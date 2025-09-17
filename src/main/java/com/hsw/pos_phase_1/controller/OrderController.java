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


    @GetMapping("/{orderId}")
    public ResponseEntity<BaseUIResponse<Order>> getOrderById(@PathVariable Long orderId, @RequestHeader("userId")Long userId) {
        Order order = orderService.getOrderById(orderId, userId); // throws if not found

        BaseUIResponse<Order> response = new BaseUIResponse<>();
        response.setResponsePayload(order);
        response.setMessage("Order fetched successfully");
        response.setStatus("SUCCESS");
        response.setCode("ORDER_FETCHED");

        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<BaseUIResponse<List<Order>>> getAllOrders(@RequestHeader("userId")Long userId) {
        List<Order> allOrders = orderService.getAllOrders(userId);

        BaseUIResponse<List<Order>> response = new BaseUIResponse<>();
        response.setResponsePayload(allOrders);
        response.setMessage("Orders fetched successfully");
        response.setStatus("SUCCESS");
        response.setCode("ORDERS_FETCHED");

        return ResponseEntity.ok(response);
    }

/*
-- Anonymous user (guest orders)
    INSERT INTO phase_1.users (user_id, loyalty_number, phone_number, secret_answer, secret_question, username)
    VALUES (0, NULL, NULL, NULL, NULL, 'anonymous');

-- Admin user (full permissions)
    INSERT INTO phase_1.users (user_id, loyalty_number, phone_number, secret_answer, secret_question, username)
    VALUES (1, 1001, '9999999999', 'adminAnswer', 'What is your role?', 'admin');
*/

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


    @PutMapping("/{orderId}")
    public ResponseEntity<BaseUIResponse<Order>> updateOrder(@PathVariable Long orderId, @RequestBody Order updatedOrder, @RequestHeader("userId")Long userId) {

        Order savedOrder = orderService.updateOrder(orderId, updatedOrder, userId);

        BaseUIResponse<Order> response = new BaseUIResponse<>();
        response.setResponsePayload(savedOrder);
        response.setMessage("Order updated successfully");
        response.setStatus("SUCCESS");
        response.setCode("ORDER_UPDATED");

        return ResponseEntity.ok(response);
    }



    @DeleteMapping("/{orderId}")
    public ResponseEntity<BaseUIResponse<String>> deleteOrder(@PathVariable Long orderId, @RequestHeader("userId")Long userId) {
        BaseUIResponse<String> response = new BaseUIResponse<>();
       if(orderService.deleteOrderById(orderId, userId)) {


        response.setResponsePayload("Order with ID " + orderId + " deleted successfully.");
        response.setMessage("Order deleted successfully");
        response.setStatus("SUCCESS");
        response.setCode("ORDER_DELETED");

        return ResponseEntity.ok(response);
       }
        response.setResponsePayload("Order with ID " + orderId + "not deleted successfully.");
        response.setMessage("Order not deleted successfully");
        response.setStatus("FAILED");
        response.setCode("ORDER_NOT_DELETED");

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
