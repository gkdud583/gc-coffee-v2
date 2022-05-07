package com.example.gccoffee.web.controller;

import com.example.gccoffee.domain.order.Order;
import com.example.gccoffee.domain.orderItem.OrderItem;
import com.example.gccoffee.exception.CustomException;
import com.example.gccoffee.service.order.OrderService;
import com.example.gccoffee.web.OrderApiController;
import com.example.gccoffee.web.dto.OrderItemRequest;
import com.example.gccoffee.web.dto.OrderItemResponse;
import com.example.gccoffee.web.dto.OrderRequest;
import com.example.gccoffee.web.dto.OrderResponse;
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
import static com.example.gccoffee.domain.order.OrderStatus.ACCEPTED;
import static com.example.gccoffee.exception.ErrorCode.NOT_ENOUGH_STOCK_QUANTITY;
import static com.example.gccoffee.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = OrderApiController.class)
@DisplayName("OrderApiController 클래스의")
public class OrderApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class order메서드는 {

        @Test
        @DisplayName("주문을 저장하고 주문 정보를 반환한다")
        void 주문을_저장하고_주문_정보를_반환한다() throws Exception {
            // given
            OrderItem orderItem = new OrderItem(1L, 10000, 1);
            Order order = new Order(1L, "test@gmail.com", "테스트 주소", "123-12", ACCEPTED, Arrays.asList(orderItem), now(), null);
            String requestJsonString = objectToJson(new OrderRequest(order.getEmail(), order.getAddress(), order.getPostcode(), Arrays.asList(new OrderItemRequest(orderItem.getProductId(), orderItem.getPrice(), orderItem.getQuantity()))));
            String responseJsonString = objectToJson(new OrderResponse(order.getOrderId(), order.getEmail(), order.getAddress(), order.getPostcode(), Arrays.asList(new OrderItemResponse(orderItem.getProductId(), orderItem.getPrice(), orderItem.getQuantity())), order.getCreatedAt()));
            given(orderService.order(anyString(), anyString(), anyString(), anyList()))
                    .willReturn(order);

            // when, then
            mockMvc.perform(post("/api/v1/orders")
                            .content(requestJsonString)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(responseJsonString));

            // then
            verify(orderService).order(anyString(), anyString(), anyString(), anyList());
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 이메일이_null이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest(null, "테스트 주소", "123-12", Arrays.asList(new OrderItemRequest(1L, 10000, 1))));

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
                assertThat(mvcResult.getResolvedException().getMessage()).contains("이메일은 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 이메일_형식이_맞지않다면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest("aldjkf392043@", "테스트 주소", "123-12", Arrays.asList(new OrderItemRequest(1L, 10000, 1))));

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andReturn();
                assertThat(mvcResult.getResolvedException().getMessage()).contains("이메일 형식이 맞지 않습니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 이메일이_빈_값이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest("", "테스트 주소", "123-12", Arrays.asList(new OrderItemRequest(1L, 10000, 1))));

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
                assertThat(mvcResult.getResolvedException().getMessage()).contains("이메일은 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 이메일이_공백이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest(" ", "테스트 주소", "123-12", Arrays.asList(new OrderItemRequest(1L, 10000, 1))));

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
                assertThat(mvcResult.getResolvedException().getMessage()).contains("이메일은 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 주소가_null이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest("test@gmail.com", null, "123-12", Arrays.asList(new OrderItemRequest(1L, 10000, 1))));

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
                assertThat(mvcResult.getResolvedException().getMessage()).contains("주소는 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 주소가_빈_값이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest("test@gmail.com", "", "123-12", Arrays.asList(new OrderItemRequest(1L, 10000, 1))));

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
                assertThat(mvcResult.getResolvedException().getMessage()).contains("주소는 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 주소가_공백이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest("test@gmail.com", " ", "123-12", Arrays.asList(new OrderItemRequest(1L, 10000, 1))));

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
                assertThat(mvcResult.getResolvedException().getMessage()).contains("주소는 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 우편번호가_null이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest("test@gmail.com", "테스트 주소", null, Arrays.asList(new OrderItemRequest(1L, 10000, 1))));

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
                assertThat(mvcResult.getResolvedException().getMessage()).contains("우편번호는 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 우편번호가_빈_값이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest("test@gmail.com", "테스트 주소", "", Arrays.asList(new OrderItemRequest(1L, 10000, 1))));

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
                assertThat(mvcResult.getResolvedException().getMessage()).contains("우편번호는 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 우편번호가_공백이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest("test@gmail.com", "테스트 주소", " ", Arrays.asList(new OrderItemRequest(1L, 10000, 1))));

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
                assertThat(mvcResult.getResolvedException().getMessage()).contains("우편번호는 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 주문상품이_null이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest("test@gmail.com", "테스트 주소", "123-12", null));

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
                assertThat(mvcResult.getResolvedException().getMessage()).contains("주문 상품은 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 주문상품이_빈_값이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest("test@gmail.com", "테스트 주소", "123-12", Arrays.asList()));

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                                    .andExpect(status().isBadRequest())
                                    .andReturn();
                assertThat(mvcResult.getResolvedException().getMessage()).contains("주문 상품은 필수 입력입니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 주문_상품_아이디가_null이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest("test@gmail.com", "테스트 주소", "123-12", Arrays.asList(new OrderItemRequest(null, 10000, 1))));

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
                assertThat(mvcResult.getResolvedException().getMessage()).contains("주문 상품 아이디가 null이 될 수 없습니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 주문_상품_가격이_0보다_작다면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest("test@gmail.com", "테스트 주소", "123-12", Arrays.asList(new OrderItemRequest(1L, -10000, 1))));

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
                assertThat(mvcResult.getResolvedException().getMessage()).contains("가격은 최소 0원이어야 합니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 주문_상품_수량이_0보다_작다면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest("test@gmail.com", "테스트 주소", "123-12", Arrays.asList(new OrderItemRequest(1L, 10000, -10))));

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andReturn();
                assertThat(mvcResult.getResolvedException().getMessage()).contains("주문 상품 수량은 최소 1개이어야 합니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 주문_상품_수량이_0이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest("test@gmail.com", "테스트 주소", "123-12", Arrays.asList(new OrderItemRequest(1L, 10000, 0))));

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
                assertThat(mvcResult.getResolvedException().getMessage()).contains("주문 상품 수량은 최소 1개이어야 합니다.");
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 존재하지_않는_상품이라면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest("test@gmail.com", "테스트 주소", "123-12", Arrays.asList(new OrderItemRequest(1L, 10000, 1))));
                willThrow(new CustomException(PRODUCT_NOT_FOUND))
                        .given(orderService).order(anyString(), anyString(), anyString(), anyList());

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
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
        class 상품_재고보다_주문_수량이_더많다면 {

            @Test
            @DisplayName("예외 응답을 반환한다")
            void 예외_응답을_반환한다() throws Exception {
                // given
                String requestJsonString = objectToJson(new OrderRequest("test@gmail.com", "테스트 주소", "123-12", Arrays.asList(new OrderItemRequest(1L, 10000, 1))));
                willThrow(new CustomException(NOT_ENOUGH_STOCK_QUANTITY))
                        .given(orderService).order(anyString(), anyString(), anyString(), anyList());

                // when, then
                MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders")
                                .content(requestJsonString)
                                .contentType(APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andReturn();

                // then
                assertThat(mvcResult.getResolvedException().getMessage()).contains("재고 수량보다 주문 상품 수량이 더 많습니다.");
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
