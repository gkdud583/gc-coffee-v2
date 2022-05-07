package com.example.gccoffee.web.dto;

import com.example.gccoffee.domain.orderItem.OrderItem;

public class OrderItemResponse {
    private final Long productId;
    private final int price;
    private final int quantity;

    public OrderItemResponse(Long productId, int price, int quantity) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderItemResponse from(OrderItem orderItem) {
        return new OrderItemResponse(orderItem.getProductId(), orderItem.getPrice(), orderItem.getQuantity());
    }
}
