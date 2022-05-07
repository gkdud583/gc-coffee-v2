package com.example.gccoffee.repository.orderItem;

import com.example.gccoffee.domain.order.Order;
import com.example.gccoffee.domain.orderItem.OrderItem;
import com.example.gccoffee.domain.product.Product;
import com.example.gccoffee.repository.order.OrderJdbcRepository;
import com.example.gccoffee.repository.order.OrderRepository;
import com.example.gccoffee.repository.product.ProductJdbcRepository;
import com.example.gccoffee.repository.product.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import static com.example.gccoffee.Category.ROASTED_BEAN;
import static org.assertj.core.api.Assertions.assertThat;

@org.junit.jupiter.api.Order(3)
@EnableAutoConfiguration
@SpringBootTest(classes = {ProductJdbcRepository.class, OrderJdbcRepository.class, OrderItemJdbcRepository.class })
@Transactional
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayName("OrderItemRepository 클래스의")
public class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class save메서드는 {

        @Test
        @DisplayName("주문 상품을 저장하고 저장된 주문 상품을 반환한다")
        void 주문_상품을_저장하고_저장된_주문_상품을_반환한다() {
            // given
            Product product = productRepository.save(new Product(ROASTED_BEAN, "커피원두1", 10000, 10, "커피원두입니다."));
            OrderItem orderItem = new OrderItem(product.getProductId(), 1000, 10);
            Order order = orderRepository.save(new Order("test@gmail.com", "테스트 주소", "123-12", Arrays.asList(orderItem)));

            // when
            OrderItem savedOrderItem = orderItemRepository.save(order.getOrderId(), orderItem);

            // then
            assertThat(savedOrderItem.getProductId()).isEqualTo(orderItem.getProductId());
            assertThat(savedOrderItem.getPrice()).isEqualTo(orderItem.getPrice());
            assertThat(savedOrderItem.getQuantity()).isEqualTo(orderItem.getQuantity());
        }
    }
}
