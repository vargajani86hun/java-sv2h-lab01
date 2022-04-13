package webshop;

import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class UserDao {

    private JdbcTemplate jdbcTemplate;

    public UserDao(MariaDbDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Optional<User> findUserByName(String name) {
        List<User> usersFound =  jdbcTemplate.query("select * from users where name = ?", (rs, rowNum) -> {
            long id = rs.getLong("id");
            String email = rs.getString("email");
            int password = rs.getInt("password");
            return new User(id, name, password, email);
        }, name);
        return usersFound.isEmpty() ? Optional.empty() : Optional.of(usersFound.get(0));
    }

    public void insertUser(String userName, int password, String email) {
            jdbcTemplate.update("insert into users (user_name, password, email) values (?, ?, ?)",
                    userName, password, email);
        }


}
