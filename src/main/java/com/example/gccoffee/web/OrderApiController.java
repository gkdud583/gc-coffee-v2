package com.example.gccoffee.web;

import com.example.gccoffee.service.order.OrderService;
import com.example.gccoffee.web.dto.OrderRequest;
import com.example.gccoffee.web.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RequestMapping("/api/v1/orders")
@RestController
public class OrderApiController {
    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> order(@RequestBody @Valid OrderRequest orderRequest) {
        return ResponseEntity.ok(OrderResponse.from(
                orderService.order(orderRequest.getEmail(), orderRequest.getAddress(), orderRequest.getPostcode(), orderRequest.getOrderItems())));
    }
}
