package com.example.gccoffee.service.orderItem;

import com.example.gccoffee.domain.orderItem.OrderItem;

public interface OrderItemService {
    OrderItem save(Long orderId, Long productId, int price, int quantity);
}
