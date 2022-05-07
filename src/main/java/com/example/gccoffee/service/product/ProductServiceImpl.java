package com.example.gccoffee.service.product;

import com.example.gccoffee.Category;
import com.example.gccoffee.domain.product.Product;
import com.example.gccoffee.exception.CustomException;
import com.example.gccoffee.repository.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static com.example.gccoffee.exception.ErrorCode.*;

@Transactional
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Product save(Category category, String productName, int price, int stockQuantity, String description) {
        Product product = new Product(category, productName, price, stockQuantity, description);
        return productRepository.save(product);
    }

    @Override
    public void updateStockQuantity(Long productId, int quantity) {
        Product product = findById(productId);
        if (product.getStockQuantity() < quantity) {
            throw new CustomException(NOT_ENOUGH_STOCK_QUANTITY);
        }
        productRepository.updateStockQuantity(productId, product.getStockQuantity() - quantity);
    }

    @Override
    public void deleteAll() {
        productRepository.deleteAll();
    }

    @Override
    public Product update(Long productId, Category category, String productName, int price, int stockQuantity, String description) {
        if (!isExists(productId)) {
            throw new CustomException(PRODUCT_NOT_FOUND);
        }
        return productRepository.update(productId, category, productName, price, stockQuantity, description);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));
    }

    private boolean isExists(Long id) {
        return productRepository.findById(id).isPresent();
    }
}
