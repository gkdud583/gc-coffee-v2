package com.example.gccoffee.repository.product;

import com.example.gccoffee.domain.product.Product;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import static com.example.gccoffee.Category.ROASTED_BEAN;
import static org.assertj.core.api.Assertions.assertThat;

@org.junit.jupiter.api.Order(2)
@EnableAutoConfiguration
@SpringBootTest(classes = ProductJdbcRepository.class)
@Transactional
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayName("ProductRepository 클래스의")
public class ProductRepositoryTest {

    @Autowired
    private ProductJdbcRepository productRepository;

    @Nested
    @Order(1)
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class save메서드는 {

        @Test
        @DisplayName("상품을 저장하고 저장된 상품을 반환한다")
        void 상품을_저장하고_저장된_상품을_반환한다() {
            // given
            Product product = new Product(ROASTED_BEAN, "커피 원두1", 1000, 10, "커피 원두입니다.");

            // when
            Product savedProduct = productRepository.save(product);

            // then
            assertThat(savedProduct.getCategory()).isEqualTo(product.getCategory());
            assertThat(savedProduct.getProductName()).isEqualTo(product.getProductName());
            assertThat(savedProduct.getDescription()).isEqualTo(product.getDescription());
            assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());
            assertThat(savedProduct.getStockQuantity()).isEqualTo(product.getStockQuantity());
        }
    }

    @Nested
    @Order(3)
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class findAll메서드는 {

        @Test
        @DisplayName("전체 상품을 조회하고 반환한다")
        void 전체_상품을_조회하고_반환한다() {
            // given
            Product savedProduct = productRepository.save(new Product(ROASTED_BEAN, "커피 원두1", 1000, 10, "커피 원두입니다."));

            // when
            List<Product> products = productRepository.findAll();

            // then
            assertThat(products.size()).isEqualTo(1);
            assertThat(products.get(0)).isEqualTo(savedProduct);
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 저장된_상품이_없다면 {

            @Test
            @DisplayName("빈 리스트를 반환한다")
            void 빈_리스트를_반환한다() {
                // given

                // when
                List<Product> products = productRepository.findAll();

                // then
                assertThat(products.size()).isEqualTo(0);
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
            Product savedProduct1 = productRepository.save(new Product(ROASTED_BEAN, name, 10000, 10, "커피 원두입니다."));
            Product savedProduct2 = productRepository.save(new Product(ROASTED_BEAN, name, 10000, 10, "커피 원두입니다."));

            // when
            List<Product> products = productRepository.findByName(name);

            // then
            assertThat(products.size()).isEqualTo(2);
            assertThat(products.get(0)).isEqualTo(savedProduct1);
            assertThat(products.get(1)).isEqualTo(savedProduct2);
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 이름이_같은_상품이_없다면 {

            @Test
            @DisplayName("빈 리스트를 반환한다")
            void 빈_리스트를_반환한다() {
                // given

                // when
                List<Product> products = productRepository.findAll();

                // then
                assertThat(products.size()).isEqualTo(0);
            }
        }
    }

    @Nested
    @Order(2)
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class findById메서드는 {

        @Test
        @DisplayName("상품 아이디로 상품을 조회하고 반환한다")
        void 상품_아이디로_상품을_조회하고_반환한다() {
            // given
            Product savedProduct = productRepository.save(new Product(ROASTED_BEAN, "커피 원두1", 1000, 10, "커피 원두입니다."));

            // when
            Optional<Product> product = productRepository.findById(savedProduct.getProductId());

            // then
            assertThat(product.get()).isEqualTo(savedProduct);
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 아이디에_해당되는_상품이_없다면 {

            @Test
            @DisplayName("empty를 반환한다")
            void empty를_반환한다() {
                // given

                // when
                Optional<Product> product = productRepository.findById(1L);

                // then
                assertThat(product).isEmpty();
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class findByCategory메서드는 {

        @Test
        @DisplayName("상품 카테고리로 상품을 조회하고 상품을 반환한다")
        void 상품_카테고리로_상품을_조회하고_반환한다() {
            // given
            Product savedProduct = productRepository.save(new Product(ROASTED_BEAN, "커피 원두1", 1000, 10, "커피 원두입니다."));

            // when
            List<Product> products = productRepository.findByCategory(savedProduct.getCategory());

            // then
            assertThat(products.size()).isEqualTo(1);
            assertThat(products.get(0)).isEqualTo(savedProduct);
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 카테고리에_해당하는_상품이_없다면 {

            @Test
            @DisplayName("빈 리스트를 반환한다")
            void 빈_리스트를_반환한다() {
                // given

                // when
                List<Product> products = productRepository.findByCategory(ROASTED_BEAN);

                // then
                assertThat(products.size()).isEqualTo(0);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class updateStockQuantity메서드는 {

        @Test
        @DisplayName("재고 수량을 수정한다")
        void 재고_수량을_수정한다() {
            // given
            int stockQuantity = 1;
            Product savedProduct = productRepository.save(new Product(ROASTED_BEAN, "커피 원두1", 1000, 10, "커피 원두입니다."));

            // when
            productRepository.updateStockQuantity(savedProduct.getProductId(), stockQuantity);

            // then
            assertThat(productRepository.findById(savedProduct.getProductId()).get().getStockQuantity()).isEqualTo(stockQuantity);
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class deleteAll메서드는 {

        @Test
        @DisplayName("전체 상품을 삭제한다")
        void 전체_상품을_삭제한다() {
            // given
            productRepository.save(new Product(ROASTED_BEAN, "커피원두1", 1000, 10, "커피 원두입니다."));
            productRepository.save(new Product(ROASTED_BEAN, "커피원두2", 1500, 20, "커피 원두입니다."));

            // when
            productRepository.deleteAll();

            // then
            List<Product> products = productRepository.findAll();
            assertThat(products.size()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class update메서드는 {

        @Test
        @DisplayName("상품 아이디로 조회한 상품의 정보를 수정하고 수정된 상품을 반환한다")
        void 상품_아이디로_조회한_상품의_정보를_수정하고_수정된_상품을_반화한다() {
            // given
            Product savedProduct = productRepository.save(new Product(ROASTED_BEAN, "커피 원두1", 10000, 10, "커피 원두입니다."));
            Product expectedProduct = new Product(savedProduct.getProductId(), ROASTED_BEAN, "커피 원두2", 10000, 10, "커피 원두입니다.", savedProduct.getCreatedAt(), savedProduct.getUpdatedAt());

            // when
            Product updatedProduct = productRepository.update(savedProduct.getProductId(), expectedProduct.getCategory(), expectedProduct.getProductName(), expectedProduct.getPrice(), expectedProduct.getStockQuantity(), expectedProduct.getDescription());

            // then
            assertThat(updatedProduct).isEqualTo(expectedProduct);
        }
    }
}