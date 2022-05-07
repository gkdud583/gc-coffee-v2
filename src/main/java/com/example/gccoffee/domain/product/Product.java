package com.example.gccoffee.domain.product;

import com.example.gccoffee.Category;
import java.time.LocalDateTime;
import java.util.Objects;

public class Product {
    private final Long productId;
    private final Category category;
    private final String productName;
    private final int price;
    private final int stockQuantity;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Product(Category category, String productName, int price, int stockQuantity, String description) {
        this.productId = null;
        this.category = category;
        this.productName = productName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = null;
    }

    public Product(Long productId, Category category, String productName, int price, int stockQuantity, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.productId = productId;
        this.category = category;
        this.productName = productName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Category getCategory() {
        return category;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", category=" + category +
                ", stockQuantity=" + stockQuantity +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
