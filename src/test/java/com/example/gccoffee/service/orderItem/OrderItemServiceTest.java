package com.example.gccoffee.service.orderItem;

import com.example.gccoffee.domain.orderItem.OrderItem;
import com.example.gccoffee.repository.orderItem.OrderItemRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderItemService 클래스의")
public class OrderItemServiceTest {
    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class save메서드는 {
        @Test
        @DisplayName("주문상품을 저장하고 저장된 주문상품을 반환한다")
        void 주문상품을_저장하고_저장된_주문상품을_반환한다() {
            // given
            OrderItem createdOrderItem = new OrderItem(1L, 1L, 1L, 10000, 10, now(), null);
            given(orderItemRepository.save(anyLong(), any(OrderItem.class)))
                    .willReturn(createdOrderItem);

            // when
            OrderItem savedOrderItem = orderItemService.save(1L, createdOrderItem.getProductId(), createdOrderItem.getPrice(), createdOrderItem.getQuantity());

            // then
            assertThat(savedOrderItem).isEqualTo(createdOrderItem);
        }
    }
}
