package com.example.gccoffee.web.dto;

import com.example.gccoffee.Category;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductSaveRequest {
    @NotNull(message = "카테고리는 필수 입력입니다.")
    private final Category category;
    @NotBlank(message = "상품 이름은 필수 입력입니다.")
    private final String productName;
    @Min(value = 1, message = "가격은 최소 1원이어야 합니다.")
    private final int price;
    @Min(value = 1, message = "상품 수량은 최소 1개이어야 합니다.")
    private final int stockQuantity;
    @NotBlank(message = "상품 설명은 필수 입력입니다.")
    private final String description;

    public ProductSaveRequest(Category category, String productName, int price, int stockQuantity, String description) {
        this.category = category;
        this.productName = productName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getDescription() {
        return description;
    }
}
