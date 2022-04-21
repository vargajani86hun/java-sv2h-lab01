package webshop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testCreateWithoutId() {
        Product product = new Product("Saw", 3600);

        assertAll(
                () -> assertEquals("Saw", product.getName()),
                () -> assertEquals(3600, product.getPrice())
        );
    }

    @Test
    void testCreateWithId() {
        Product product = new Product(1, "Drill", 18100);

        assertAll(
                () -> assertEquals(1, product.getId()),
                () -> assertEquals("Drill", product.getName()),
                () -> assertEquals(18100, product.getPrice())
        );
    }
}