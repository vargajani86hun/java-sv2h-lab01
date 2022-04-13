package webshop;

import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class OrderDao {

    private JdbcTemplate jdbcTemplate;

    public OrderDao(MariaDbDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insertItem(User user, Item item) {
        jdbcTemplate.update("insert into orders (user_name, product_name, amount, date) values (?, ?, ?, ?)",
                user.getName(), item.getProduct().getName(), item.getAmount(), Timestamp.valueOf(LocalDateTime.now()));
    }
}
