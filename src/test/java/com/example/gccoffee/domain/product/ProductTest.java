package com.example.gccoffee.domain.product;

import com.example.gccoffee.exception.CustomException;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import static com.example.gccoffee.Category.ROASTED_BEAN;
import static com.example.gccoffee.exception.ErrorCode.NOT_ENOUGH_STOCK_QUANTITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Product 클래스의")
public class ProductTest {

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class order메서드는 {

        @Test
        @DisplayName("상품의 재고 수량을 변경한다")
        void 상품의_재고_수량을_변경한다() {
            // given
            Product product = new Product(ROASTED_BEAN, "커피 원두", 1000, 10, "커피 원두입니다");
            int orderProductQuantity = 5;

            // when
            product.order(orderProductQuantity);

            // then
            assertThat(product.getStockQuantity()).isEqualTo(5);
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품의_재고_수량보다_주문_수량이_많다면{

            @Test
            @DisplayName("예외를 던진다")
            void 예외를_던진다() {
                // given
                Product product = new Product(1L, ROASTED_BEAN, "커피 원두", 1000, 10, "커피 원두입니다", LocalDateTime.now(), null);

                // when, then
                assertThatThrownBy(() -> product.order(product.getStockQuantity() + 10))
                        .isInstanceOf(CustomException.class)
                        .hasMessage(NOT_ENOUGH_STOCK_QUANTITY.getDetail());

            }
        }
    }
}
