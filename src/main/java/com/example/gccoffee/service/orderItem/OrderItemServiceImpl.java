package com.example.gccoffee.service.orderItem;

import com.example.gccoffee.domain.orderItem.OrderItem;
import com.example.gccoffee.repository.orderItem.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public OrderItem save(Long orderId, Long productId, int price, int quantity) {
        return orderItemRepository.save(orderId, new OrderItem(productId, price, quantity));
    }
}
