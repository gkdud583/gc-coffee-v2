package com.example.gccoffee.service.order;

import com.example.gccoffee.domain.order.Order;
import com.example.gccoffee.domain.orderItem.OrderItem;
import com.example.gccoffee.exception.CustomException;
import com.example.gccoffee.repository.order.OrderRepository;
import com.example.gccoffee.service.orderItem.OrderItemService;
import com.example.gccoffee.service.product.ProductService;
import com.example.gccoffee.web.dto.OrderItemRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import static com.example.gccoffee.domain.order.OrderStatus.ACCEPTED;
import static com.example.gccoffee.exception.ErrorCode.NOT_ENOUGH_STOCK_QUANTITY;
import static com.example.gccoffee.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService 클래스의")
public class OrderServiceTest {
    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private ProductService productService;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class order메서드는 {

        @Test
        @DisplayName("주문을 저장하고 저장된 주문을 반환한다")
        void 주문을_저장하고_저장된_주문을_반환한다() {
            // given
            OrderItem orderItem = new OrderItem(1L, 1L, 1L, 1000, 10, now(), null);
            Order order = new Order(1L, "test@gmail.com", "주소", "123-12", ACCEPTED, Arrays.asList(orderItem), now(), null);
            given(orderRepository.save(any(Order.class)))
                    .willReturn(order);
            // when
            Order savedOrder = orderService.order(order.getEmail(), order.getAddress(), order.getPostcode(), Arrays.asList(new OrderItemRequest(1L, orderItem.getPrice(), orderItem.getPrice())));

            // then
            verify(orderRepository).save(any(Order.class));
            verify(orderItemService).save(anyLong(), anyLong(), anyInt(), anyInt());
            verify(productService).updateStockQuantity(anyLong(), anyInt());
            assertThat(savedOrder).isEqualTo(order);
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 존재하지_않는_상품이라면 {

            @Test
            @DisplayName("예외를 던진다")
            void 예외를_던진다() {
                // given
                OrderItem orderItem = new OrderItem(1L, 1L, 1L, 1000, 10, now(), null);
                Order order = new Order(1L, "test@gmail.com", "주소", "123-12", ACCEPTED, Arrays.asList(orderItem), now(), null);
                willThrow(new CustomException(PRODUCT_NOT_FOUND))
                        .given(productService).updateStockQuantity(anyLong(), anyInt());

                // when, then
                assertThatThrownBy(() -> orderService.order(order.getEmail(), order.getAddress(), order.getPostcode(), Arrays.asList(new OrderItemRequest(orderItem.getProductId(), orderItem.getPrice(), orderItem.getQuantity()))))
                        .isInstanceOf(CustomException.class)
                        .hasMessage(PRODUCT_NOT_FOUND.getDetail());
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 재고_수량보다_주문_수량이_더많다면 {

            @Test
            @DisplayName("예외를 던진다")
            void 예외를_던진다() {
                // given
                OrderItem orderItem = new OrderItem(1L, 1L, 1L, 1000, 10, now(), null);
                Order order = new Order(1L, "test@gmail.com", "주소", "123-12", ACCEPTED, Arrays.asList(orderItem), now(), null);
                willThrow(new CustomException(NOT_ENOUGH_STOCK_QUANTITY))
                        .given(productService).updateStockQuantity(anyLong(), anyInt());

                // when, then
                assertThatThrownBy(() -> orderService.order(order.getEmail(), order.getAddress(), order.getPostcode(), Arrays.asList(new OrderItemRequest(orderItem.getProductId(), orderItem.getPrice(), orderItem.getQuantity()))))
                        .isInstanceOf(CustomException.class)
                        .hasMessage(NOT_ENOUGH_STOCK_QUANTITY.getDetail());
            }
        }
    }
}
