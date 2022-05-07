package com.example.gccoffee.web.controller;

import com.example.gccoffee.Category;
import com.example.gccoffee.domain.product.Product;
import com.example.gccoffee.exception.CustomException;
import com.example.gccoffee.service.product.ProductService;
import com.example.gccoffee.web.ProductApiController;
import com.example.gccoffee.web.dto.ProductResponse;
import com.example.gccoffee.web.dto.ProductSaveRequest;
import com.example.gccoffee.web.dto.ProductUpdateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import static com.example.gccoffee.Category.ROASTED_BEAN;
import static com.example.gccoffee.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ProductApiController.class)
@DisplayName("ProductApiController 클래스의")
public class ProductApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class findAll메서드는 {

        @Nested
        @DisplayName("이름 파라미터가 null이 아니라면")
        class 이름_파라미터가_null이_아니라면 {

            @Test
            @DisplayName("이름으로 상품을 조회하고 반환한다")
            void 이름으로_상품을_조회하고_반환한다() throws Exception {
                // given
                String name = "testName";
                Product product = new Product(1L, ROASTED_BEAN, name, 10000, 10, "커피 원두입니다", now(), null);
                List<Product> products = Arrays.asList(product);
                String responseJsonString = objectToJson(Arrays.asList(new ProductResponse(product.getProductId(), product.getProductName(), product.getCategory(), product.getPrice(), product.getStockQuantity(), product.getDescription())));
                given(productService.findByName(anyString()))
                        .willReturn(products);

                // when, then
                mockMvc.perform(get("/api/v1/products?name="+name))
                        .andExpect(status().isOk())
                        .andExpect(content().json(responseJsonString));

                // then
                verify(productService).findByName(anyString());
            }
        }

        @Nested
        @DisplayName("이름으로 조회 결과 상품이 없다면")
        class 이름으로_조회_결과_상품이_없다면 {

            @Test
            @DisplayName("빈 리스트를 반환한다")
            void 빈_리스트를_반환한다() throws Exception {
                // given
                String name = "testName";
                Product product = new Product(1L, ROASTED_BEAN, name, 10000, 10, "커피 원두입니다", now(), null);
                String responseJsonString = objectToJson(Arrays.asList());
                given(productService.findByName(anyString()))
                        .willReturn(Arrays.asList());

                // when, then
                mockMvc.perform(get("/api/v1/products?name="+name))
                        .andExpect(status().isOk())
                        .andExpect(content().json(responseJsonString));
            }
        }

        @Nested
        @DisplayName("카테고리 파라미터가 null이 아니라면")
        class 카테고리_파라미터가_null이_아니라면 {

            @Test
            @DisplayName("카테고리로 상품을 조회하고 반환한다")
            void 카테고리로_상품을_조회하고_반환한다() throws Exception {
                // given
                Category category = ROASTED_BEAN;
                Product product = new Product(1L, category, "커피원두", 10000, 10, "커피 원두입니다", now(), null);
                List<Product> products = Arrays.asList(product);
                String responseJsonString = objectToJson(Arrays.asList(new ProductResponse(product.getProductId(), product.getProductName(), product.getCategory(), product.getPrice(), product.getStockQuantity(), product.getDescription())));
                given(productService.findByCategory(any(Category.class)))
                        .willReturn(products);

                // when, then
                mockMvc.perform(get("/api/v1/products?category="+category))
                        .andExpect(status().isOk())
                        .andExpect(content().json(responseJsonString));

                // then
                verify(productService).findByCategory(any(Category.class));
            }

            @Nested
            @DisplayName("카테고리로 조회 결과 상품이 없다면")
            class 카테고리로_조회_결과_상품이_없다면 {

                @Test
                @DisplayName("빈 리스트를 반환한다")
                void 빈_리스트를_반환한다() throws Exception {
                    // given
                    Category category = ROASTED_BEAN;
                    Product product = new Product(1L, category, "커피원두", 10000, 10, "커피 원두입니다", now(), null);
                    String responseJsonString = objectToJson(Arrays.asList());
                    given(productService.findByName(anyString()))
                            .willReturn(Arrays.asList());

                    // when, then
                    mockMvc.perform(get("/api/v1/products?category="+category))
                            .andExpect(status().isOk())
                            .andExpect(content().json(responseJsonString));
                }
            }
        }

        @Nested
        @DisplayName("이름, 카테고리 파라미터가 모두 null이라면")
        class 이름_카테고리_파라미터가_모두_null이라면 {

            @Test
            @DisplayName("전체 상품을 조회하고 반환한다")
            void 전체_상품을_조회하고_반환한다() throws Exception {
                // given
                List<Product> products = Arrays.asList(
                        new Product(1L, ROASTED_BEAN, "상품1", 10000, 10, "커피 원두1", now(), null),
                        new Product(2L, ROASTED_BEAN, "상품2", 20000, 10, "커피 원두2", now(), null));
                List<ProductResponse> productResponses = products.stream().map((product) -> ProductResponse.from(product))
                        .collect(Collectors.toList());
                given(productService.findAll())
                        .willReturn(products);

                // when, then
                mockMvc.perform(get("/api/v1/products"))
                        .andExpect(status().isOk())
                        .andExpect(content().json(objectToJson(productResponses)));

                // then
                verify(productService).findAll();
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 전체_조회_결과_상품이_없다면 {

            @Test
            @DisplayName("빈 리스트를 반환한다")
            void 빈_리스트를_반환한다() throws Exception {
                // given
                List products = Collections.EMPTY_LIST;
                given(productService.findAll())
                        .willReturn(products);

                // when, then
                mockMvc.perform(get("/api/v1/products"))
                        .andExpect(status().isOk())
                        .andExpect(content().json(objectToJson(products)));
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class save메서드는 {

        @Test
        @DisplayName("상품을 저장하고 저장된 상품을 반환한다")
        void 상품을_저장하고_저장된_상품을_반환한다() throws Exception {
            // given
            ProductSaveRequest request = new ProductSaveRequest(ROASTED_BEAN, "커피 원두1", 10000, 10, "커피 원두1입니다.");
            String requestJsonString = objectToJson(request);
            Product product = new Product(1L, request.getCategory(), request.getProductName(), request.getPrice(), request.getStockQuantity(), request.getDescription(), now(), null);
            ProductResponse response = ProductResponse.from(product);
            String responseJsonString = objectToJson(response);
            given(productService.save(any(Category.class), anyString(), anyInt(), anyInt(), anyString()))
                    .willReturn(product);

            // when, then
            mockMvc.perform(post("/api/v1/products")
                            .content(requestJsonString)
                            .contentType(APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(content().json(responseJsonString));

            // then
            verify(productService).save(any(Category.class), anyString(), anyInt(), anyInt(), anyString());
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 카테고리가_null이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                ProductSaveRequest request = new ProductSaveRequest(null, "커피 원두1", 10000, 10, "커피 원두1입니다.");
                String requestJsonString = objectToJson(request);

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("카테고리는 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_이름이_null이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                ProductSaveRequest request = new ProductSaveRequest(ROASTED_BEAN, null, 10000, 10, "커피 원두1입니다.");
                String requestJsonString = objectToJson(request);

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품 이름은 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_이름이_빈_값이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                ProductSaveRequest request = new ProductSaveRequest(ROASTED_BEAN, "", 10000, 10, "커피 원두1입니다.");
                String requestJsonString = objectToJson(request);

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품 이름은 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_이름이_공백이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                ProductSaveRequest request = new ProductSaveRequest(ROASTED_BEAN, " ", 10000, 10, "커피 원두1입니다.");
                String requestJsonString = objectToJson(request);

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품 이름은 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_설명이_null이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                ProductSaveRequest request = new ProductSaveRequest(ROASTED_BEAN, "커피 원두1", 1000, 10, null);
                String requestJsonString = objectToJson(request);

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품 설명은 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_설명이_빈_값이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                ProductSaveRequest request = new ProductSaveRequest(ROASTED_BEAN, "커피 원두1", 1000, 10, "");
                String requestJsonString = objectToJson(request);

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품 설명은 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_설명이_공백이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                ProductSaveRequest request = new ProductSaveRequest(ROASTED_BEAN, "커피 원두1", 10000, 10, " ");
                String requestJsonString = objectToJson(request);

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품 설명은 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_가격이_0보다_작다면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                ProductSaveRequest request = new ProductSaveRequest(ROASTED_BEAN, "커피 원두1", -10000, 10, " ");
                String requestJsonString = objectToJson(request);

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("가격은 최소 1원이어야 합니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_가격이_0이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                ProductSaveRequest request = new ProductSaveRequest(ROASTED_BEAN, "커피 원두1", 0, 10, " ");
                String requestJsonString = objectToJson(request);

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("가격은 최소 1원이어야 합니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_수량이_0보다_작다면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                ProductSaveRequest request = new ProductSaveRequest(ROASTED_BEAN, "커피 원두1", 10000, -10, " ");
                String requestJsonString = objectToJson(request);

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품 수량은 최소 1개이어야 합니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_수량이_0이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                ProductSaveRequest request = new ProductSaveRequest(ROASTED_BEAN, "커피 원두1", 1000, 0, " ");
                String requestJsonString = objectToJson(request);

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품 수량은 최소 1개이어야 합니다.");
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class deleteAll메서드는 {

        @Test
        @DisplayName("모든 상품을 삭제한다")
        void 모든_상품을_삭제한다() throws Exception {
            // given

            // when, then
            mockMvc.perform(delete("/api/v1/products"))
                    .andExpect(status().isOk());
            // then
            verify(productService).deleteAll();
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class update메서드는 {

        @Test
        @DisplayName("상품 아이디로 조회한 상품의 정보를 수정하고 수정된 상품을 반환한다")
        void 상품_아이디로_조회한_상품의_정보를_수정하고_수정된_상품을_반환한다() throws Exception {
            // given
            Product product = new Product(1L, ROASTED_BEAN, "변경된 이름", 2000, 10, "변경된 상품입니다.", now(), null);

            String requestJsonString = objectToJson(new ProductUpdateRequest(product.getProductId(), ROASTED_BEAN, product.getProductName(), product.getPrice(), product.getStockQuantity(), product.getDescription()));
            String responseJsonString = objectToJson(new ProductResponse(product.getProductId(), product.getProductName(), product.getCategory(), product.getPrice(), product.getStockQuantity(), product.getDescription()));

            given(productService.update(anyLong(), any(Category.class), any(String.class), anyInt(), anyInt(), any(String.class)))
                    .willReturn(product);

            // when, then
            mockMvc.perform(put("/api/v1/products")
                            .content(requestJsonString)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(responseJsonString));

            // then
            verify(productService).update(anyLong(), any(Category.class), any(String.class), anyInt(), anyInt(), any(String.class));
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 아이디에_해당하는_상품이_없다면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {

                // given
                Product product = new Product(1L, ROASTED_BEAN, "변경된 이름", 2000, 10, "변경된 상품입니다.", now(), null);
                String requestJsonString = objectToJson(new ProductUpdateRequest(product.getProductId(), ROASTED_BEAN, product.getProductName(), product.getPrice(), product.getStockQuantity(), product.getDescription()));
                given(productService.update(anyLong(), any(Category.class), any(String.class), anyInt(), anyInt(), any(String.class)))
                        .willThrow(new CustomException(PRODUCT_NOT_FOUND));

                // when, then
                MvcResult mvcResult = mockMvc.perform(put("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품을 찾을 수 없습니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_아이디가_null이_주어지면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new ProductUpdateRequest(null, ROASTED_BEAN, "변경된 이름", 2000, 10, "변경된 상품입니다."));

                // when, then
                MvcResult mvcResult = mockMvc.perform(put("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품 아이디는 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 카테고리가_null이_주어지면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new ProductUpdateRequest(1L, null, "변경된 이름", 2000, 10, "변경된 상품입니다."));

                // when, then
                MvcResult mvcResult = mockMvc.perform(put("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품 카테고리는 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_이름이_null이_주어지면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new ProductUpdateRequest(1L, ROASTED_BEAN, null, 2000, 10, "변경된 상품입니다."));

                // when, then
                MvcResult mvcResult = mockMvc.perform(put("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품 이름은 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_이름이_공백이_주어지면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new ProductUpdateRequest(1L, ROASTED_BEAN, " ", 2000, 10, "변경된 상품입니다."));

                // when, then
                MvcResult mvcResult = mockMvc.perform(put("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품 이름은 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_이름이_빈_값이_주어지면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new ProductUpdateRequest(1L, ROASTED_BEAN, "", 2000, 10, "변경된 상품입니다."));

                // when, then
                MvcResult mvcResult = mockMvc.perform(put("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품 이름은 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_가격이_0보다_작다면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new ProductUpdateRequest(1L, ROASTED_BEAN, "변경된 이름", -1000, 10, "변경된 상품입니다."));

                // when, then
                MvcResult mvcResult = mockMvc.perform(put("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("가격은 최소 1원이어야 합니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_가격이_0이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new ProductUpdateRequest(1L, ROASTED_BEAN, "변경된 이름", 0, 10, "변경된 상품입니다."));

                // when, then
                MvcResult mvcResult = mockMvc.perform(put("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("가격은 최소 1원이어야 합니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_재고_수량이_음수라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new ProductUpdateRequest(1L, ROASTED_BEAN, "변경된 이름", 1000, -10, "변경된 상품입니다."));

                // when, then
                MvcResult mvcResult = mockMvc.perform(put("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품 수량은 최소 0개이어야 합니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_설명이_null이_주어지면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new ProductUpdateRequest(1L, ROASTED_BEAN, "변경된 이름", 1000, 10, null));

                // when, then
                MvcResult mvcResult = mockMvc.perform(put("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품 설명은 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_설명이_공백이_주어지면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new ProductUpdateRequest(1L, ROASTED_BEAN, "변경된 이름", 1000, 10, " "));

                // when, then
                MvcResult mvcResult = mockMvc.perform(put("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품 설명은 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 상품_설명이_빈_값이_주어지면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new ProductUpdateRequest(1L, ROASTED_BEAN, "변경된 이름", 1000, 10, ""));

                // when, then
                MvcResult mvcResult = mockMvc.perform(put("/api/v1/products")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품 설명은 필수 입력입니다.");
            }
        }
    }
    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class findById메서드는 {

        @Test
        @DisplayName("아이디로 상품을 조회하고 반환한다")
        void 아이디로_상품을_조회하고_반환한다() throws Exception {
            // given
            Product product = new Product(1L, ROASTED_BEAN, "커피 원두", 10000, 10, "커피 원두입니다.", now(), null);
            String responseJsonString = objectToJson(new ProductResponse(product.getProductId(), product.getProductName(), product.getCategory(), product.getPrice(), product.getStockQuantity(), product.getDescription()));
            given(productService.findById(anyLong()))
                    .willReturn(product);

            // when, then
            mockMvc.perform(get("/api/v1/products/" + product.getProductId()))
                    .andExpect(status().isOk())
                    .andExpect(content().json(responseJsonString));
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 아이디에_해당하는_상품이_없다면 {

            @Test
            @DisplayName("예외를 반환한다")
            void 예외를_반환한다() throws Exception {
                // given
                Product product = new Product(1L, ROASTED_BEAN, "커피 원두", 10000, 10, "커피 원두입니다.", now(), null);
                given(productService.findById(anyLong()))
                        .willThrow(new CustomException(PRODUCT_NOT_FOUND));

                // when, then
                MvcResult mvcResult = mockMvc.perform(get("/api/v1/products/" + product.getProductId()))
                        .andExpect(status().isNotFound())
                        .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("상품을 찾을 수 없습니다.");
            }
        }
    }

    private String objectToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}