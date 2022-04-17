package webshop;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class WebShopMain {
    private final String UNDERLINED_AND_BOLD_COLORSCHEME = "\u001B[4;1m";
    private final String NOT_UNDERLINED_AND_BOLD_COLORSCHEME = "\u001B[24;22m";
    private static final String FRAME_COLORSCHEME = "\u001B[30;43m";
    private static final String LINE_INPUT_COLORSCHEME = "\u001B[33;49m";
    private static final List<String> MENUITEMS_LOGIN = Arrays.asList(
            "[1] Belépés",
            "[2] Regisztráció",
            "[3] Kilépés");

    private static final List<String> MENUITEMS_WEBSHOP = Arrays.asList(
            "[1] Webshop termékeinek megtekintése",
            "[2] Kosár tartalmának megtekintése",
            "[3] Termék kosárba helyezése",
            "[4] Termék kivétele a kosárból",
            "[5] Termék mennyiségének növelése a kosárban",
            "[6] Termék mennyiségének csökkentése a kosárban",
            "[7] Rendelés véglegesítése",
            "[8] Kilépés");

    private ShopService shopService;

    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/webshop?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("klaradb");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot reach DataBase!", sqle);
        }

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();

        WebShopMain webShopMain = new WebShopMain();
        webShopMain.shopService = new ShopService(dataSource);
        webShopMain.runLoginMenu();
    }

    private void runLoginMenu() {
        boolean exit = false;
        do {
            TableForUX loginMenu = new TableForUX(51, MENUITEMS_LOGIN, List.of("Belépés csak regisztrált ügyfeleknek", "M E N Ü"));
            loginMenu.print();
            switch (getSelectedMenuItem(MENUITEMS_LOGIN.size())) {
                case 1:
                    Login();
                    break;
                case 2:
                    registerUser();
                    break;
                case 3:
                    exit = true;
            }
        } while (!exit);
        messagePrint("Kilépés");
    }

    private void registerUser() {
        messagePrint("Regisztráció");
        List<String> input = inputsToList("Felhasználónév:", "Jelszó:", "Email:");
        shopService.registerUser(input.get(0), input.get(1), input.get(2));
        messagePrint("Helló " + input.get(0) + ", a regisztráció sikeres volt.");
        messagePrint("Extra Kedvezményekért iratkozz fel hírlevelünkre is!");
    }

    private void Login() {
        messagePrint("Belépés");
        List<String> input = inputsToList("Felhasználónév:", "Jelszó:");
        shopService.logIn(input.get(0), input.get(1));
        runShoppingMenu();
    }

    private int getSelectedMenuItem(int menuLinesNumber) {
        Scanner scanner = new Scanner(System.in);
        int selectedMenuItem = 0;
        do {
            frameAndTextPrint("Válassz a menüből!");
            String s = scanner.nextLine();
            try {
                selectedMenuItem = Integer.parseInt(s);
            } catch (IllegalArgumentException iae) {
                selectedMenuItem = 0;
            }
            if (selectedMenuItem <= 0 || selectedMenuItem > menuLinesNumber) {
                messagePrint("Nincs ilyen menüpont!");
            }
        } while (selectedMenuItem <= 0 || selectedMenuItem > menuLinesNumber);
        return selectedMenuItem;
    }

    private List<String> inputsToList(String... texts) {
        Scanner scanner = new Scanner(System.in);
        List<String> result = new ArrayList<>();
        for (String actual : texts) {
            frameAndTextPrint(actual);
            result.add(scanner.nextLine());
        }
        return result;
    }

    private void runShoppingMenu() {
        boolean exit = false;
        do {
            String headingLine = String.format("%74s", ("Helló " + shopService.getUserName() + ", ma minden IS akciós!"));
            TableForUX shoppingMenu = new TableForUX(76, MENUITEMS_WEBSHOP, List.of(headingLine, "Struktúraváltó Barkácsbolt Webshop"));
            shoppingMenu.print();
            switch (getSelectedMenuItem(MENUITEMS_WEBSHOP.size())) {
                case 1:
                    printProducts();
                    break;
                case 2:
                    printCart();
                    break;
                case 3:
                    putToCart();
                    break;
                case 4:
                    deleteFromCart();
                    break;
                case 5:
                    increaseAmount();
                    break;
                case 6:
                    decreaseAmount();
                    break;
                case 7:
                    finalizeOrder();
                    break;
                case 8:
                    exit = true;
            }
        } while (!exit);
        messagePrint("Kilépés");
    }

    private void printProducts() {
        String heading = String.format("%10s        %-24s%9s", "Cikkszám", "Termék neve", "Ár");
        List<String> productRows = new ArrayList<>();
        for (Product actual : shopService.getProductList()) {
            productRows.add(String.format("%10s        %-24s%6s Ft", actual.getId(), actual.getName(), actual.getPrice()));
        }
        TableForUX products = new TableForUX(66, productRows, List.of("Struktúraváltó Barkácsbolt Webshop termékei", heading));
        products.print();
    }

    private void printCart() {
        List<String> cartRows = new ArrayList<>();
        int sum = 0;
        for (Item actual : shopService.getUserCart().contentOfCart()) {
            cartRows.add(String.format("%10s        %-24s%6s Ft%10s db %8s Ft", actual.getProduct().getId(), actual.getProduct().getName(),
                    actual.getProduct().getPrice(), actual.getAmount(), actual.getSumPrice()));
            sum += actual.getSumPrice();
        }
        cartRows.add(String.format("%86s", ""));
        cartRows.add(String.format("%81s%5s", "A kosár összértéke: " + sum + " Ft", ""));
        String heading = String.format("%10s        %-24s%9s%13s%12s", "Cikkszám", "Termék neve", "Ár", "Mennyiség", "Érték");
        TableForUX cart = new TableForUX(88, cartRows, List.of("A kosár aktuális tartalma", heading));
        cart.print();
    }

    private void putToCart() {
        printCart();
        printProducts();
        List<String> input = inputsToList("Megvásárolni kívánt termék cikkszáma:", "Mennyiség:");
        long productId = Integer.parseInt(input.get(0));
        int amount = Integer.parseInt(input.get(1));
        shopService.addItem(productId, amount);
        messagePrint(" A(z) " + highlightIt(shopService.getItem(productId).getProduct().getName())
                + " termékből " + highlightIt(amount) + " darabot betettél a kosárba.");
    }

    private void deleteFromCart() {
        printCart();
        long productId = Integer.parseInt(inputsToList("Törölni kívánt termék cikkszáma:").get(0));
        String productToDelete = shopService.getItem(productId).getProduct().getName();
        shopService.removeItem(productId);
        messagePrint(" A(z) " + highlightIt(productToDelete) + " terméket kivetted a kosárból.");
    }

    private void increaseAmount() {
        printCart();
        List<String> input = inputsToList("Melyik cikkszámú termék mennyiségét növelnéd?", "Mennyiség:");
        long productId = Integer.parseInt(input.get(0));
        int amount = Integer.parseInt(input.get(1));
        shopService.modifyAmount(productId, amount);
        messagePrint(" A(z) " + highlightIt(shopService.getItem(productId).getProduct().getName()) + " termék mennyiségét "
                + highlightIt(amount) + " darabbal növelted a kosárban.");
    }

    private void decreaseAmount() {
        printCart();
        List<String> input = inputsToList("Melyik cikkszámú termék mennyiségét csökkentenéd?", "Mennyiség:");
        long productId = Integer.parseInt(input.get(0));
        int amount = Integer.parseInt(input.get(1));
        shopService.modifyAmount(productId, -1* amount);
        messagePrint(" A(z) " + highlightIt(shopService.getItem(productId).getProduct().getName()) + " termék mennyiségét "
                + highlightIt(amount) + " darabbal csökkentetted a kosárban.");
    }

    private void finalizeOrder() {
        printCart();
        if (inputsToList("Véglegesíted a rendelést? (i/n)").get(0).equals("i")) {
            shopService.order();
            messagePrint("Köszönjük a megrendelést!");
            messagePrint("Iratkozz fel hírlevelünkre, elárasztunk spamekkel!");
        }
    }

    private void frameAndTextPrint(String text) {
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " " + text + " ");
    }

    private void messagePrint(String text) {
        System.out.println(FRAME_COLORSCHEME + " " + text + " " + LINE_INPUT_COLORSCHEME);
    }

    private String highlightIt(String text) {
        return UNDERLINED_AND_BOLD_COLORSCHEME + text + NOT_UNDERLINED_AND_BOLD_COLORSCHEME;
    }

    private String highlightIt(int number) {
        return UNDERLINED_AND_BOLD_COLORSCHEME + number + NOT_UNDERLINED_AND_BOLD_COLORSCHEME;
    }
}


