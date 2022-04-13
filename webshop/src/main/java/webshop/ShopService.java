package webshop;

import org.mariadb.jdbc.MariaDbDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShopService {

    private ProductDao productDao;
    private UserDao userDao;
    private OrderDao orderDao;
    private User user;

    public ShopService(MariaDbDataSource dataSource) {
        this.productDao = new ProductDao(dataSource);
        this.userDao = new UserDao(dataSource);
        this.orderDao = new OrderDao(dataSource);
    }

    public void registerUser(String name, String password, String email) {
        if (userDao.findUserByName(name).isEmpty()) {
            int psw = (name + password).hashCode();
            userDao.insertUser(name, psw, email);
        } else {
            throw new IllegalArgumentException("Name is already taken!");
        }
    }

    public void logIn(String name, String password) {
        Optional<User> userFound = userDao.findUserByName(name);
        if (userFound.isEmpty()) {
            throw new IllegalArgumentException("Username is wrong!");
        } else if (userFound.get().getPassword() != (name + password).hashCode()) {
            throw new IllegalArgumentException("Password is Invalid!");
        } else {
            user = userFound.get();
            user.setLogIn(true);
        }
    }

    public void addItem(Product product) {
        user.getCart().addItem(new Item(product, 1));
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
    }

    private List<String> readFile(Path path) {
        try {
            return Files.readAllLines(path);
        } catch (IOException ioe) {
            throw new IllegalStateException("Can not read File!");
        }
    }

    public void fillProducts(Path path) {
        for (String line: readFile(path)) {
            String[] parts = line.split(";");
            String name = parts[0];
            int price = Integer.parseInt(parts[1]);
            Product product = new Product(name, price);
            productDao.insertProduct(product);
        }
    }

    public List<String> getProductList() {
        List<String> productList = new ArrayList<>();
        for (Product product: productDao.getProducts()) {
            productList.add(String.format("%d %s %d", product.getId(), product.getName(), product.getPrice()));
        }
        return productList;
    }
}
