package com.example.gccoffee.service.product;

import com.example.gccoffee.Category;
import com.example.gccoffee.domain.product.Product;
import com.example.gccoffee.exception.CustomException;
import com.example.gccoffee.repository.product.ProductRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static com.example.gccoffee.Category.ROASTED_BEAN;
import static com.example.gccoffee.exception.ErrorCode.*;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService 클래스의")
public class ProductServiceTest {
    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class findAll메서드는 {

        @Test
        @DisplayName("전체 상품을 조회하고 반환한다")
        void 전체_상품을_조회하고_반환한다() {
            // given
            List<Product> createdProducts = Arrays.asList(
                    new Product(1L, ROASTED_BEAN, "상품1", 10000, 10, "커피 원두1", now(), null),
                    new Product(2L, ROASTED_BEAN, "상품2", 20000, 10,"커피 원두2", now(), null));
            given(productRepository.findAll())
                    .willReturn(createdProducts);

            // when
            List<Product> products = productService.findAll();

            // then
            verify(productRepository).findAll();
            assertThat(products).isEqualTo(products);
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 저장된_상품이_없다면 {

            @Test
            @DisplayName("빈 리스트를 반환한다")
            void 빈_리스트를_반환한다() {
                // given
                given(productRepository.findAll())
                        .willReturn(new ArrayList<>());
                // when
                List<Product> products = productService.findAll();

                // then
                assertThat(products.size()).isEqualTo(0);
            }
        }
    }
    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class save메서드는 {

        @Test
        @DisplayName("상품을 저장하고 저장된 상품을 반환한다")
        void 상품을_저장하고_저장된_상품을_반환한다() {
            // given
            Product product = new Product(1L, ROASTED_BEAN, "커피 원두1", 10000, 10, "커피 원두입니다.", now(), now());
            given(productRepository.save(any(Product.class)))
                    .willReturn(product);

            // when
            Product savedProduct = productService.save(product.getCategory(), product.getProductName(), product.getPrice(), product.getStockQuantity(), product.getDescription());

            // then
            assertThat(savedProduct).isEqualTo(product);
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class updateStockQuantity메서드는 {

        @Test
        @DisplayName("상품의 재고 수량을 변경한다")
        void 상품의_재고_수량을_변경한다() {
            // given
            Long productId = 1L;
            int stockQuantity = 10;
            given(productRepository.findById(anyLong()))
                    .willReturn(Optional.of(new Product(productId, ROASTED_BEAN, "커피원두1", 1000, stockQuantity, "커피 원두입니다.", now(), null)));
            // when
            productService.updateStockQuantity(productId, stockQuantity);

            // then
            verify(productRepository).findById(anyLong());
            verify(productRepository).updateStockQuantity(anyLong(), anyInt());
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품이_없다면 {

            @Test
            @DisplayName("예외를 던진다")
            void 예외를_던진다() {
                // given
                given(productRepository.findById(anyLong()))
                        .willReturn(Optional.empty());

                // when, then
                assertThatThrownBy(() -> productService.updateStockQuantity(1L, 10))
                        .isInstanceOf(CustomException.class)
                        .hasMessage(PRODUCT_NOT_FOUND.getDetail());
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 재고_수량보다_주문_수량이_많다면 {

            @Test
            @DisplayName("예외를 던진다")
            void 예외를_던진다() {
                // given
                given(productRepository.findById(anyLong()))
                        .willReturn(Optional.of(new Product(1L, ROASTED_BEAN, "커피원두1", 1000, 100, "커피 원두입니다.", now(), null)));

                // when, then
                assertThatThrownBy(() -> productService.updateStockQuantity(1L, 200))
                        .isInstanceOf(CustomException.class)
                        .hasMessage(NOT_ENOUGH_STOCK_QUANTITY.getDetail());
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class deleteAll메서드는 {
        @Test
        @DisplayName("전체 상품을 삭제한다")
        void 전체_상품을_삭제한다() {
            // given

            // when
            productService.deleteAll();

            // then
            verify(productRepository).deleteAll();
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class update메서드는 {

        @Test
        @DisplayName("상품 아이디로 조회한 상품의 정보를 수정하고 수정된 상품을 반환한다")
        void 상품_아이디로_조회한_상품의_정보를_수정하고_수정된_상품을_반환한다() {
            // given
            Product product = new Product(1L, ROASTED_BEAN, "커피 원두", 10000, 10, "커피 원두입니다.", now(), null);
            given(productRepository.findById(anyLong()))
                    .willReturn(Optional.of(product));
            given(productRepository.update(product.getProductId(), product.getCategory(), product.getProductName(), product.getPrice(), product.getStockQuantity(), product.getDescription()))
                    .willReturn(product);
            // when
            Product updatedProduct = productService.update(product.getProductId(), product.getCategory(), product.getProductName(), product.getPrice(), product.getStockQuantity(), product.getDescription());

            // then
            verify(productRepository).update(anyLong(), nullable(Category.class), nullable(String.class), anyInt(), anyInt(), nullable(String.class));
            assertThat(updatedProduct).isEqualTo(product);
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 아이디에_해당하는_상품이_없다면 {

            @Test
            @DisplayName("예외를 던진다")
            void 예외를_던진다() {
                // given
                Product product = new Product(1L, ROASTED_BEAN, "커피 원두", 10000, 10, "커피 원두입니다.", now(), null);
                given(productRepository.findById(product.getProductId()))
                        .willReturn(Optional.empty());

                // when, then
                assertThatThrownBy(() -> productService.update(product.getProductId(), product.getCategory(), product.getProductName(), product.getPrice(), product.getStockQuantity(), product.getDescription()))
                        .isInstanceOf(CustomException.class)
                        .hasMessage(PRODUCT_NOT_FOUND.getDetail());
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class findByName메서드는 {

        @Test
        @DisplayName("이름으로 상품을 조회하고 반환한다")
        void 이름으로_상품을_조회하고_반환한다() {
            // given
            String name = "testName";
            List<Product> createdProducts = Arrays.asList(new Product(1L, ROASTED_BEAN, name, 10000, 10, "커피 원두입니다", now(), null));
            given(productRepository.findByName(anyString()))
                    .willReturn(createdProducts);
            // when
            List<Product> products = productService.findByName(name);

            // then
            verify(productRepository).findByName(anyString());
            assertThat(products).isEqualTo(createdProducts);
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 저장된_상품이_없다면 {

            @Test
            @DisplayName("빈 리스트를 반환한다")
            void 빈_리스트를_반환한다() {
                // given
                given(productRepository.findByName(anyString()))
                        .willReturn(new ArrayList<>());
                // when
                List<Product> products = productService.findByName("testName");

                // then
                assertThat(products.size()).isEqualTo(0);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class findByCategory메서드는 {

        @Test
        @DisplayName("카테고리로 상품을 조회하고 반환한다")
        void 카테고리로_상품을_조회하고_반환한다() {
            // given
            Category category = ROASTED_BEAN;
            List<Product> createdProducts = Arrays.asList(new Product(1L, category, "커피원두", 10000, 10, "커피 원두입니다", now(), null));
            given(productRepository.findByCategory(any(Category.class)))
                    .willReturn(createdProducts);
            // when
            List<Product> products = productService.findByCategory(category);

            // then
            verify(productRepository).findByCategory(any(Category.class));
            assertThat(products).isEqualTo(createdProducts);
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 저장된_상품이_없다면 {

            @Test
            @DisplayName("빈 리스트를 반환한다")
            void 빈_리스트를_반환한다() {
                // given
                given(productRepository.findByCategory(any(Category.class)))
                        .willReturn(new ArrayList<>());
                // when
                List<Product> products = productService.findByCategory(ROASTED_BEAN);

                // then
                assertThat(products.size()).isEqualTo(0);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class findById메서드는 {

        @Test
        @DisplayName("아이디로 상품을 조회하고 반환한다")
        void 아이디로_상품을_조회하고_반환한다() {
            // given
            Product createdProduct = new Product(1L, ROASTED_BEAN, "커피 원두", 10000, 10, "커피 원두입니다", now(), null);
            given(productRepository.findById(anyLong()))
                    .willReturn(Optional.of(createdProduct));

            // when
            Product product = productService.findById(createdProduct.getProductId());

            // then
            verify(productRepository).findById(anyLong());
            assertThat(product).isEqualTo(createdProduct);
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 조회된_상품이_없다면 {
            @Test
            @DisplayName("상품을 찾을 수 없다는 예외를 던진다")
            void 상품을_찾을_수_없다는_예외를_던진다() {
                // given
                given(productRepository.findById(anyLong()))
                        .willReturn(Optional.empty());

                // when, then
                assertThatThrownBy(() -> productService.findById(1L))
                        .isInstanceOf(CustomException.class)
                        .hasMessage(PRODUCT_NOT_FOUND.getDetail());
            }
        }
    }
}
