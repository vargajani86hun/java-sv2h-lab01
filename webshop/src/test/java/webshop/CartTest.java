package webshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;
    private Product hammer = new Product("Hammer", 2500);
    private Product saw = new Product("Saw", 3200);
    private Product drill = new Product("Drill", 18100);

    @BeforeEach
    void init() {
        cart = new Cart();

        cart.addItem(new Item(drill, 1));
    }

    @Test
    void testCreate() {
        Cart cart = new Cart();

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testAddNewItem() {
        cart.addItem(new Item(hammer, 2));

        assertEquals(2, cart.getItems().size());
    }

    @Test
    void testAddItemTwice() {
        cart.addItem(new Item(saw, 2));
        cart.addItem(new Item(saw, 3));

        assertAll(
                () -> assertEquals(2, cart.getItems().size()),
                () -> assertEquals(saw, cart.getItems().get(1).getProduct()),
                () -> assertEquals(5, cart.getItems().get(1).getAmount())
        );
    }
}