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
        List<User> usersFound =  jdbcTemplate.query("SELECT * FROM users WHERE user_name = ?;", (rs, rowNum) -> {
            long id = rs.getLong("id");
            String email = rs.getString("email");
            int password = rs.getInt("password");
            return new User(id, name, password, email);
        }, name);
        return getUserIfExist(usersFound);
    }

    public long insertUser(String userName, int psw, String email) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {//language=sql
                    PreparedStatement ps = con.prepareStatement("INSERT INTO users (`user_name`, `password`, `email`) VALUES (?, ?, ?);",
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, userName);
                    ps.setInt(2, psw);
                    ps.setString(3, email);
                    return ps;
                }
                , keyHolder);
        return keyHolder.getKey().longValue();
    }

    public Optional<User> findUserByEmail(String email) {//language=sql
        List<User> findUsers = jdbcTemplate.query("SELECT * FROM users WHERE email = ?;",
                (rs, rowNum) -> new User(rs.getLong("id"),
                        rs.getString("user_name"),
                        rs.getInt("password"),
                        rs.getString("email")
                ), email);
        return getUserIfExist(findUsers);
    }

    private Optional<User> getUserIfExist(List<User> findUsers) {
        if (findUsers.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(findUsers.get(0));
        }
    }

    public List<User> getRegisteredUsers() {
        return jdbcTemplate.query("SELECT * FROM users;", (rs, rowNum) -> {
            long id = rs.getLong("id");
            String userName = rs.getString("user_name");
            int password = rs.getInt("password");
            String email = rs.getString("email");
            return new User(id, userName, password, email);
        });
    }
}
