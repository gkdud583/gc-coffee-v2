package com.example.gccoffee.repository.order;

import com.example.gccoffee.domain.order.Order;

public interface OrderRepository {
    Order save(Order order);
}
