package webshop;

import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class UserDao {

    private JdbcTemplate jdbcTemplate;

    public UserDao(MariaDbDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Optional<User> findUserByName(String name) {//language=sql
        List<User> usersFound =  jdbcTemplate.query("select * from users where name = ?;", (rs, rowNum) -> {
            long id = rs.getLong("id");
            String email = rs.getString("email");
            int password = rs.getInt("password");
            return new User(id, name, password, email);
        }, name);
        return usersFound.isEmpty() ? Optional.empty() : Optional.of(usersFound.get(0));
    }

    public long insertUser(String userName, int psw, String email) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {//language=sql
                    PreparedStatement ps = con.prepareStatement("insert into users (`user_name`, `password`, `email`) values (?, ?, ?);",
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, userName);
                    ps.setInt(2, psw);
                    ps.setString(3, email);
                    return ps;
                }
                , keyHolder);
        return keyHolder.getKey().longValue();
    }

    public User findUserByEmail(String email) {//language=sql
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = ?;",
                (rs, rowNum) -> new User(rs.getLong("id"),
                        rs.getString("user_name"),
                        rs.getInt("password"),
                        rs.getString("email")
                ), email);
    }
}
