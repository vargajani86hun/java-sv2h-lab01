package webshop;

import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    public ProductDao(MariaDbDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insertProduct(Product product) {
        jdbcTemplate.update("insert into products (name, price) values (?, ?)",
                product.getName(), product.getPrice());
    }
}
