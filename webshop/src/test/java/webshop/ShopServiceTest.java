package webshop;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    MariaDbDataSource dataSource = new MariaDbDataSource();
    ShopService shopService;

    @BeforeEach
    void initDataBase() {
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/webshop_test?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("klaradb");
        } catch (
                SQLException sqle) {
            throw new IllegalStateException("Cannot reach DataBase!", sqle);
        }
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();
        shopService = new ShopService(dataSource);
    }

    @Test
    void testRegisterUser() {
        shopService.registerUser("Nagy Sándor", "rodnáS ygaN", "nagy@sandor.hu");
        assertEquals(1, shopService.getRegisteredUsers().size());
        assertEquals("Nagy Sándor", shopService.getRegisteredUsers().get(0).getName());
        assertEquals("nagy@sandor.hu", shopService.getRegisteredUsers().get(0).getEmail());
        assertEquals(("Nagy Sándor" + "rodnáS ygaN").hashCode(), shopService.getRegisteredUsers().get(0).getPassword());
        assertTrue(shopService.getRegisteredUsers().get(0).getCart().contentOfCart().isEmpty());
    }

    @Test
    void testRegisterAndLoginUser() {
        shopService.registerUser("Kis Péter", "retéP siK", "kis@peter.hu");
        shopService.logIn("Kis Péter", "retéP siK");
        assertEquals("Kis Péter", shopService.getUserName());
        assertTrue(shopService.getContentOfCart().isEmpty());
    }

    @Test
    void testGetProductList() {
        assertEquals(10, shopService.getProductList().size());
        assertEquals("Csavarbehajtó", shopService.getProductList().get(4).getName());
        assertEquals(22000, shopService.getProductList().get(4).getPrice());
    }

    @Test
    void testAddItem() {
        long productId1 = shopService.getProductList().get(5).getId();
        long productId2 = shopService.getProductList().get(7).getId();
        shopService.registerUser("Zöld Piroska", "aksoriP dlöZ", "zold@piroska.hu");
        shopService.logIn("Zöld Piroska", "aksoriP dlöZ");
        shopService.addItem(productId1, 2);
        shopService.addItem(productId2, 1);
        assertEquals(2, shopService.getContentOfCart().size());
        assertEquals("Tetőléc", shopService.getContentOfCart().get(0).getProduct().getName());
        assertEquals(1200, shopService.getContentOfCart().get(0).getProduct().getPrice());
        assertEquals(2, shopService.getContentOfCart().get(0).getAmount());
    }

    @Test
    void testRemoveItem() {
        long productId1 = shopService.getProductList().get(5).getId();
        long productId2 = shopService.getProductList().get(7).getId();
        shopService.registerUser("Zöld Piroska", "aksoriP dlöZ", "zold@piroska.hu");
        shopService.logIn("Zöld Piroska", "aksoriP dlöZ");
        shopService.addItem(productId1, 2);
        shopService.addItem(productId2, 1);
        assertEquals(2, shopService.getContentOfCart().size());
        shopService.removeItem(productId1);
        assertEquals(1, shopService.getContentOfCart().size());

    }

    @Test
    void testIncreaseAmount() {
        long productId = shopService.getProductList().get(5).getId();
        shopService.registerUser("Zöld Piroska", "aksoriP dlöZ", "zold@piroska.hu");
        shopService.logIn("Zöld Piroska", "aksoriP dlöZ");
        shopService.addItem(productId, 2);
        shopService.increaseAmount(productId, 3);
        assertEquals(5, shopService.getContentOfCart().get(0).getAmount());
    }


    @Test
    void testDecreaseAmount() {
        long productId = shopService.getProductList().get(5).getId();
        shopService.registerUser("Zöld Piroska", "aksoriP dlöZ", "zold@piroska.hu");
        shopService.logIn("Zöld Piroska", "aksoriP dlöZ");
        shopService.addItem(productId, 3);
        shopService.decreaseAmount(productId, 2);
        assertEquals(1, shopService.getContentOfCart().get(0).getAmount());
    }

    @Test
    void testDecreaseAmountWithMoreThanActual() {
        long productId = shopService.getProductList().get(5).getId();
        shopService.registerUser("Zöld Piroska", "aksoriP dlöZ", "zold@piroska.hu");
        shopService.logIn("Zöld Piroska", "aksoriP dlöZ");
        shopService.addItem(productId, 3);
        assertEquals(1, shopService.getContentOfCart().size());
        shopService.decreaseAmount(productId, 4);
        assertTrue(shopService.getContentOfCart().isEmpty());
    }

    @Test
    void testGetTotalPrice() {
        long productId1 = shopService.getProductList().get(5).getId();
        long productId2 = shopService.getProductList().get(7).getId();
        shopService.registerUser("Zöld Piroska", "aksoriP dlöZ", "zold@piroska.hu");
        shopService.logIn("Zöld Piroska", "aksoriP dlöZ");
        shopService.addItem(productId1, 2);
        shopService.addItem(productId2, 1);
        assertEquals(3300, shopService.getTotalPrice());
    }

    @Test
    void testOrder() {
        long productId1 = shopService.getProductList().get(5).getId();
        long productId2 = shopService.getProductList().get(7).getId();
        shopService.registerUser("Zöld Piroska", "aksoriP dlöZ", "zold@piroska.hu");
        shopService.logIn("Zöld Piroska", "aksoriP dlöZ");
        shopService.addItem(productId1, 2);
        shopService.addItem(productId2, 1);
        shopService.order();
        assertTrue(shopService.getContentOfCart().isEmpty());
    }
}