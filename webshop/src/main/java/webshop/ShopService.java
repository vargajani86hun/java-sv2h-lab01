package webshop;

import org.mariadb.jdbc.MariaDbDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ShopService {

    private ProductDao productDao;
    private UserDao userDao;
    private OrderDao orderDao;
    private User user;
    private List<Product> productList;
    private Path path = Path.of("src/main/resources/products.csv");

    public ShopService(MariaDbDataSource dataSource) {
        productDao = new ProductDao(dataSource);
        userDao = new UserDao(dataSource);
        orderDao = new OrderDao(dataSource);
        fillProducts();
        productList = productDao.getProducts();
    }

    public void registerUser(String name, String password, String email) {
        UserValidator2 validator = new UserValidator2(userDao);
        validator.validateNewUserName(name);
        validator.validatePassword(password);
        validator.validateEmail(email);
        int psw = (name + password).hashCode();
        userDao.insertUser(name, psw, email);
    }

    public void logIn(String name, String password) {
        UserValidator validator = new UserValidator(userDao);
        validator.validateExistUserName(name);
        User userFound = userDao.findUserByName(name).get();
        validator.validatePasswordLogIn(name, password, userFound);
            user = userFound;
    }

    public void addItem(long id, int amount) {
        Product product = findProductById(id);
        user.getCart().addItem(new Item(product, amount));
    }

    public void removeItem(long id) {
        Item item = getItem(id);
        user.getCart().removeItem(item);
    }

    private Item getItem(long id) {
        return user.getCart().getItems().stream().filter(item -> id == item.getProduct().getId()).toList().get(0);
    }

    public void modifyAmount(Long id, int amount) {
        Item item = getItem(id);
        item.modifyAmount(amount);
    }

    public void order() {
        long orderId = orderDao.insertOrder(user.getId());
        for (Item item: user.getCart().getItems()) {
            orderDao.insertItem(orderId, item.getProduct().getId(), item.getAmount());
        }
        user.getCart().emptyCart();
    }

    private List<String> readFile(Path path) {
        try {
            return Files.readAllLines(path);
        } catch (IOException ioe) {
            throw new IllegalStateException("Can not read File!");
        }
    }

    public void fillProducts() {
        for (String line: readFile(path)) {
            String[] parts = line.split(";");
            String name = parts[0];
            int price = Integer.parseInt(parts[1]);
            Product product = new Product(name, price);
            productDao.insertProduct(product);
        }
    }

    public List<Product> getProductList() {
        return productList;
    }

    public List<String> getProductStringList() {
        List<String> productListString = new ArrayList<>();
        for (Product product: productList) {
            productListString.add(String.format("%d %s %d", product.getId(), product.getName(), product.getPrice()));
        }
        return productListString;
    }

    private Product findProductById(long id) {
        List<Product> productsFound = productList.stream().filter(p -> id == p.getId()).toList();
        if (productsFound.isEmpty()) {
            throw new IllegalArgumentException("Product not found!");
        } else {
            return productsFound.get(0);
        }
    }
}
