package webshop;

import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.time.LocalDateTime;

public class OrderDao {

    private JdbcTemplate jdbcTemplate;

    public OrderDao(MariaDbDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public long insertOrder(long userId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {//language=sql
            PreparedStatement ps = con.prepareStatement("INSERT INTO orders (user_id, order_date) VALUES (?, ?);", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setLong(1, userId);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public void insertItem(long orderId, long productId, int amount) {//language=sql
        jdbcTemplate.update("INSERT INTO items (order_id, product_id, amount) VALUES (?, ?, ?);",
                orderId, productId, amount);
    }
}
