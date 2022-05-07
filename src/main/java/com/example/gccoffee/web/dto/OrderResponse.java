package com.example.gccoffee.web.dto;

import com.example.gccoffee.domain.order.Order;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private final Long orderId;
    private final String email;
    private final String address;
    private final String postcode;
    private final List<OrderItemResponse> orderItems;
    private final LocalDateTime createdAt;

    public OrderResponse(Long orderId, String email, String address, String postcode, List<OrderItemResponse> orderItems, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.orderItems = orderItems;
        this.createdAt = createdAt;
    }

    public static OrderResponse from(Order order) {
        List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
                .map((orderItem) -> OrderItemResponse.from(orderItem))
                .collect(Collectors.toList());
        return new OrderResponse(order.getOrderId(), order.getEmail(), order.getAddress(), order.getPostcode(), orderItemResponses, order.getCreatedAt());
    }
}
