package com.example.gccoffee.repository.orderItem;

import com.example.gccoffee.domain.orderItem.OrderItem;

public interface OrderItemRepository {
    OrderItem save(Long orderId, OrderItem orderItem);
}
