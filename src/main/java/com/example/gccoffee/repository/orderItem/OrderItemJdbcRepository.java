package com.example.gccoffee.repository.orderItem;

import com.example.gccoffee.domain.orderItem.OrderItem;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderItemJdbcRepository implements OrderItemRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public OrderItemJdbcRepository(DataSource dataSource, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("order_items")
                .usingGeneratedKeyColumns("order_item_id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public OrderItem save(Long orderId, OrderItem orderItem) {
        Long orderItemId = insertAction.executeAndReturnKey(toOrderItemParamMap(orderId, orderItem)).longValue();
        return new OrderItem(orderItemId, orderId, orderItem.getProductId(), orderItem.getPrice(), orderItem.getQuantity(), orderItem.getCreatedAt(), orderItem.getUpdatedAt());
    }

    private Map<String, Object> toOrderItemParamMap(Long orderId, OrderItem orderItem) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("order_id", orderId);
        paramMap.put("product_id", orderItem.getProductId());
        paramMap.put("quantity", orderItem.getQuantity());
        paramMap.put("price", orderItem.getPrice());
        paramMap.put("created_at", orderItem.getCreatedAt());
        paramMap.put("updated_at", orderItem.getUpdatedAt());
        return paramMap;
    }
}
