package com.example.gccoffee.repository.order;

import com.example.gccoffee.domain.order.Order;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderJdbcRepository implements OrderRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public OrderJdbcRepository(DataSource dataSource, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("orders")
                .usingGeneratedKeyColumns("order_id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Order save(Order order) {
        Long orderId = insertAction.executeAndReturnKey(toOrderParamMap(order)).longValue();
        return new Order(orderId, order.getEmail(), order.getAddress(), order.getPostcode(), order.getOrderStatus(), order.getOrderItems(), order.getCreatedAt(), order.getUpdatedAt());
    }

    private Map<String, Object> toOrderParamMap(Order order) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("email", order.getEmail());
        paramMap.put("address", order.getAddress());
        paramMap.put("postcode", order.getPostcode());
        paramMap.put("order_status", order.getOrderStatus().toString());
        paramMap.put("created_at", order.getCreatedAt());
        paramMap.put("updated_at", order.getUpdatedAt());
        return paramMap;
    }
}
