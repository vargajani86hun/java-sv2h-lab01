package webshop;

import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    public ProductDao(MariaDbDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insertProduct(Product product) {//language=sql
        jdbcTemplate.update("insert into products (product_name, price) values (?, ?)",
                product.getName(), product.getPrice());
    }

    public List<Product> getProducts() {
        return jdbcTemplate.query("select * from products", (rs, col) -> {
            long id = rs.getLong("id");
            String name = rs.getString("product_name");
            int price = rs.getInt("price");
            return new Product(id, name, price);
        });
    }

    public Product findProductById(long id) {
        List<Product> productsFound = jdbcTemplate.query("select * from products", (rs, col) -> {
            String name = rs.getString("product_name");
            int price = rs.getInt("price");
            return new Product(id, name, price);
        });
        if (productsFound.isEmpty()) {
            throw new IllegalArgumentException("Product not found!");
        } else {
            return productsFound.get(0);
        }
    }
}
