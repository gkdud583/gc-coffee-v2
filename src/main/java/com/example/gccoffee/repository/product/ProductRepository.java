package com.example.gccoffee.repository.product;

import com.example.gccoffee.Category;
import com.example.gccoffee.domain.product.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();
    List<Product> findByName(String name);
    List<Product> findByCategory(Category category);
    Product save(Product product);
    Optional<Product> findById(Long productId);
    void updateStockQuantity(Long productId, int stockQuantity);
    void deleteAll();
    Product update(Long productId, Category category, String productName, int price, int stockQuantity, String description);
}
