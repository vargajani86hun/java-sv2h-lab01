package webshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    private Item item;

    @BeforeEach
    void init() {
        item = new Item(new Product("drill", 18100), 2);
    }

    @Test
    void testCreate() {
        Item item = new Item(new Product("hammer", 2500), 3);

        assertAll(
                () -> assertEquals(new Product("hammer", 2500), item.getProduct()),
                () -> assertEquals(7500, item.getSumPrice()),
                () -> assertEquals(3, item.getAmount())
        );
    }

    @Test
    void testIncreaseAmount() {
        item.modifyAmount(1);

        assertAll(
                () -> assertEquals(3, item.getAmount()),
                () -> assertEquals(54300, item.getSumPrice())
        );
    }

    @Test
    void testDecreaseAmount() {
        item.modifyAmount(-1);

        assertAll(
                () -> assertEquals(1, item.getAmount()),
                () -> assertEquals(18100, item.getSumPrice())
        );
    }
}