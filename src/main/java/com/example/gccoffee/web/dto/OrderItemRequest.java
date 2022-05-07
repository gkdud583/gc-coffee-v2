package com.example.gccoffee.web.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderItemRequest {
    @NotNull(message = "주문 상품 아이디가 null이 될 수 없습니다.")
    private final Long productId;
    @Min(value=0, message = "가격은 최소 0원이어야 합니다.")
    private final int price;
    @Min(value=1, message = "주문 상품 수량은 최소 1개이어야 합니다.")
    private final int quantity;

    public OrderItemRequest(Long productId, int price, int quantity) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
