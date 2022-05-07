package com.example.gccoffee.domain.orderItem;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderItem {
    private final Long orderItemId;
    private final Long orderId;
    private final Long productId;
    private final int price;
    private final int quantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public OrderItem(Long productId, int price, int quantity) {
        this.orderItemId = null;
        this.orderId = null;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = null;
    }

    public OrderItem(Long orderItemId, Long orderId, Long productId, int price, int quantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getProductId() {
        return productId;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", price=" + price +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(orderItemId, orderItem.orderItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItemId);
    }
}
