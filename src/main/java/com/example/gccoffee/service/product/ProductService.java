package com.example.gccoffee.service.product;

import com.example.gccoffee.Category;
import com.example.gccoffee.domain.product.Product;
import java.util.List;

public interface ProductService {
    List<Product> findAll();
    List<Product> findByName(String name);
    List<Product> findByCategory(Category category);
    Product findById(Long productId);
    Product save(Category category, String productName, int price, int stockQuantity, String description);
    void updateStockQuantity(Long productId, int quantity);
    void deleteAll();
    Product update(Long productId, Category category, String productName, int price, int stockQuantity, String description);
}
