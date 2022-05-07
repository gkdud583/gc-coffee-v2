package com.example.gccoffee.repository.product;

import com.example.gccoffee.Category;
import com.example.gccoffee.domain.product.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.*;
import static com.example.gccoffee.repository.Utils.toLocalDateTime;

@Repository
public class ProductJdbcRepository implements ProductRepository{
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public ProductJdbcRepository(DataSource dataSource, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("products")
                .usingGeneratedKeyColumns("product_id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Product> findAll() {
        return namedParameterJdbcTemplate.query(
                "SELECT * FROM products WHERE removed = false", productRowMapper);
    }

    @Override
    public List<Product> findByName(String name) {
        return namedParameterJdbcTemplate.query(
                "SELECT * FROM products WHERE product_name = :productName AND removed = false",
                Collections.singletonMap("productName", name),
                productRowMapper);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        List<Product> result = namedParameterJdbcTemplate.query("SELECT * FROM products WHERE product_id = :productId AND removed = false",
                Collections.singletonMap("productId", productId), productRowMapper);
        if (result.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return namedParameterJdbcTemplate.query("SELECT * FROM products WHERE category = :category AND removed = false",
                Collections.singletonMap("category", category.toString()), productRowMapper);
    }

    @Override
    public Product save(Product product) {
        Long productId = insertAction.executeAndReturnKey(toProductParamMap(product)).longValue();
        return new Product(productId, product.getCategory(), product.getProductName(), product.getPrice(), product.getStockQuantity(), product.getDescription(), product.getCreatedAt(), product.getUpdatedAt());
    }

    @Override
    public void updateStockQuantity(Long productId, int stockQuantity) {
        namedParameterJdbcTemplate.update("UPDATE products SET stock_quantity = :stockQuantity WHERE product_id = :productId AND removed = false",
                Map.of("productId", productId, "stockQuantity", stockQuantity));
    }


    @Override
    public void deleteAll() {
        namedParameterJdbcTemplate.update("UPDATE products SET removed = true", Collections.EMPTY_MAP);
    }

    @Override
    public Product update(Long productId, Category category, String productName, int price, int stockQuantity, String description) {
        namedParameterJdbcTemplate.update("UPDATE products SET category = :category, product_name = :productName, price = :price, stock_quantity = :stockQuantity, description = :description, updated_at = :updatedAt WHERE product_id = :productId AND removed = false",
                Map.of(
                "productId", productId,
                "category", category.toString(),
                "productName", productName,
                "price", price,
                "stockQuantity", stockQuantity,
                "description", description,
                "updatedAt", LocalDateTime.now()));
        List<Product> products = namedParameterJdbcTemplate.query("SELECT * FROM products WHERE product_id = :productId AND removed = false",
                Collections.singletonMap("productId", productId), productRowMapper);
        return products.get(0);
    }

    private Map<String, Object> toProductParamMap(Product product) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("product_name", product.getProductName());
        paramMap.put("category", product.getCategory().toString());
        paramMap.put("stock_quantity", product.getStockQuantity());
        paramMap.put("price", product.getPrice());
        paramMap.put("description", product.getDescription());
        paramMap.put("removed", 0);
        paramMap.put("created_at", product.getCreatedAt());
        paramMap.put("updated_at", product.getUpdatedAt());
        return paramMap;
    }

    private static final RowMapper<Product> productRowMapper = (resultSet, i) -> {
        Long productId = resultSet.getLong("product_id");
        String productName = resultSet.getString("product_name");
        Category category = Category.valueOf(resultSet.getString("category"));
        int price = resultSet.getInt("price");
        int stockQuantity = resultSet.getInt("stock_quantity");
        String description = resultSet.getString("description");
        LocalDateTime createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        LocalDateTime updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Product(productId, category, productName, price, stockQuantity, description, createdAt, updatedAt);
    };
}
