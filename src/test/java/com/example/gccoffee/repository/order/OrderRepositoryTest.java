package com.example.gccoffee.repository.order;

import com.example.gccoffee.domain.order.Order;
import com.example.gccoffee.domain.orderItem.OrderItem;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;

@org.junit.jupiter.api.Order(1)
@EnableAutoConfiguration
@SpringBootTest(classes = OrderJdbcRepository.class)
@Transactional
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayName("OrderRepository 클래스의")
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class save메서드는 {

        @Test
        @DisplayName("주문을 저장하고 저장된 주문을 반환한다")
        void 주문을_저장하고_저장된_주문을_반환한다() {
            // given
            Order order = new Order("test@gmail.com", "주소", "123-12", Arrays.asList(new OrderItem(1L, 1000, 10)));

            // when
            Order savedOrder = orderRepository.save(new Order(order.getEmail(), order.getAddress(), order.getPostcode(), order.getOrderItems()));

            // then
            assertThat(savedOrder.getEmail()).isEqualTo(order.getEmail());
            assertThat(savedOrder.getAddress()).isEqualTo(order.getAddress());
            assertThat(savedOrder.getPostcode()).isEqualTo(order.getPostcode());
            assertThat(savedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
            assertThat(savedOrder.getOrderItems()).isEqualTo(order.getOrderItems());
        }
    }
}
