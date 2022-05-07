package com.example.gccoffee.service.order;

import com.example.gccoffee.domain.order.Order;
import com.example.gccoffee.web.dto.OrderItemRequest;
import java.util.List;

public interface OrderService {
    Order order(String email, String address, String postcode, List<OrderItemRequest> orderItems);
}
