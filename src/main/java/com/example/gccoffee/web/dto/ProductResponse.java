package com.example.gccoffee.web.dto;

import com.example.gccoffee.Category;
import com.example.gccoffee.domain.product.Product;

public class ProductResponse {
    private final Long productId;
    private final String productName;
    private final Category category;
    private final int price;
    private final int stockQuantity;
    private final String description;

    public ProductResponse(Long productId, String productName, Category category, int price, int stockQuantity, String description) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getProductId(), product.getProductName(), product.getCategory(), product.getPrice(), product.getStockQuantity(), product.getDescription());
    }
}
