package webshop;

import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    public ProductDao(MariaDbDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public long insertProduct(Product product) {//language=sql
        Optional<Product> existingProduct = findProductByName(product.getName());
        if (existingProduct.isPresent()) {
            return existingProduct.get().getId();
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement("INSERT INTO products (product_name, price) VALUES (?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, product.getName());
                    ps.setInt(2, product.getPrice());
                    return ps;
                },
                keyHolder);
        return keyHolder.getKey().longValue();
    }

    public List<Product> getProducts() {//language=sql
        return jdbcTemplate.query("select * from products", (rs, col) -> {
            long id = rs.getLong("id");
            String name = rs.getString("product_name");
            int price = rs.getInt("price");
            return new Product(id, name, price);
        });
    }

    public Product findProductById(long id) {//language=sql
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

    public Optional<Product> findProductByName(String name) {//language=sql
        List<Product> foundProducts = jdbcTemplate.query("SELECT * FROM products WHERE product_name = ?;",
                (rs, rowNum) -> new Product(rs.getLong("id"),
                        rs.getString("product_name"),
                        rs.getInt("price")),
                name);
        if (foundProducts.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(foundProducts.get(0));
        }
    }
}
