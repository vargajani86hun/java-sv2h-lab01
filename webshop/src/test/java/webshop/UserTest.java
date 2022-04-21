package webshop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testCreate() {
        User user = new User(1, "John Doe", 1928345, "john.doe@gmail.com");

        assertAll(
                () -> assertEquals(1, user.getId()),
                () -> assertEquals("John Doe", user.getName()),
                () -> assertEquals(1928345, user.getPassword()),
                () -> assertEquals("john.doe@gmail.com", user.getEmail()),
                () -> assertEquals(0, user.getCart().getItems().size())
        );
    }

}