package com.example.gccoffee.service.order;

import com.example.gccoffee.domain.order.Order;
import com.example.gccoffee.domain.orderItem.OrderItem;
import com.example.gccoffee.repository.order.OrderRepository;
import com.example.gccoffee.service.orderItem.OrderItemService;
import com.example.gccoffee.service.product.ProductService;
import com.example.gccoffee.web.dto.OrderItemRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final ProductService productService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemService orderItemService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.productService = productService;
    }

    @Override
    public Order order(String email, String address, String postcode, List<OrderItemRequest> orderItemRequests) {

        Order order = orderRepository.save(new Order(email, address, postcode, null));
        List<OrderItem> orderItmes = orderItemRequests.stream()
                .map((orderItemRequest) -> {
                    productService.updateStockQuantity(orderItemRequest.getProductId(), orderItemRequest.getQuantity());
                    return orderItemService.save(order.getOrderId(), orderItemRequest.getProductId(), orderItemRequest.getPrice(), orderItemRequest.getQuantity());
                })
                .collect(Collectors.toList());
        return new Order(order.getOrderId(), order.getEmail(), order.getAddress(), order.getPostcode(), order.getOrderStatus(), orderItmes, order.getCreatedAt(), order.getUpdatedAt());
    }
}
