package com.example.gccoffee.domain.order;

import com.example.gccoffee.domain.orderItem.OrderItem;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private final Long orderId;
    private final String email;
    private final String address;
    private final String postcode;
    private final OrderStatus orderStatus;
    private final List<OrderItem> orderItems;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Order(String email, String address, String postcode, List<OrderItem> orderItems) {
        this.orderId = null;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.orderStatus = OrderStatus.ACCEPTED;
        this.orderItems = orderItems;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = null;
    }

    public Order(Long orderId, String email, String address, String postcode, OrderStatus orderStatus, List<OrderItem> orderItems, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.orderStatus = orderStatus;
        this.orderItems = orderItems;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPostcode() {
        return postcode;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", postcode='" + postcode + '\'' +
                ", orderStatus=" + orderStatus +
                ", orderItems=" + orderItems +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
