package webshop;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class WebShopMain {
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
            "[5] Termék mennyiségének növelése",
            "[6] Termék mennyiségének csökkentése",
            "[7] Rendelés véglegesítése",
            "[8] Kilépés");

    private PrintToConsole printToConsole;
    private ShopService shopService;

    public static void main(String[] args) {
        WebShopMain webShopMain = new WebShopMain();
        webShopMain.printToConsole = new PrintToConsole();

        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/webshop?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("klaradb");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot reach DataBase!", sqle);
        }

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
//        flyway.clean();
        flyway.migrate();

        webShopMain.shopService = new ShopService(dataSource);
        webShopMain.runLoginMenu();
    }

    private void runShoppingMenu() {
        boolean exit = false;
        do {
            printMenu("Struktúraváltó Barkácsbolt Webshop", MENUITEMS_WEBSHOP);
            switch (getSelectedMenuItem(8)) {
                case 1:
                    printProducts();
                    break;
                case 2:
                    printCart();
                    break;
                case 3:
                    toCart();
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
                    finalOrder();
                    break;
                case 8:
                    exit = true;
            }
        } while (!exit);
    }

    private void finalOrder() {
        printCart();
        Scanner scanner = new Scanner(System.in);
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Véglegesíted a rendelést? (i/n) ");
        String answer = scanner.nextLine();
        if (answer.equals("i")) {
            shopService.order();
            System.out.println(FRAME_COLORSCHEME + " Köszönjük a megrendelést!" + LINE_INPUT_COLORSCHEME);
            System.out.println(FRAME_COLORSCHEME + " Iratkozzon fel hírlevelünkre is, elárasztjuk spamekkel! " + LINE_INPUT_COLORSCHEME);
        }
    }

    private void decreaseAmount() {
        printCart();
        Scanner scanner = new Scanner(System.in);
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Melyk cikkszámú termék mennyiségét csökkentenéd? ");
        long productId = Integer.parseInt(scanner.nextLine());
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Mennyiség: ");
        int amount = -1 * Integer.parseInt(scanner.nextLine());
        shopService.modifyAmount(productId, amount);
        System.out.println(FRAME_COLORSCHEME + " A " + productId + " cikkszámú termékből " + (-1 * amount) + " darabot kivettünk a kosárból." + LINE_INPUT_COLORSCHEME);
    }

    private void increaseAmount() {
        printCart();
        Scanner scanner = new Scanner(System.in);
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Melyk cikkszámú termék mennyiségét növelnéd? ");
        long productId = Integer.parseInt(scanner.nextLine());
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Mennyiség: ");
        int amount = Integer.parseInt(scanner.nextLine());
        shopService.modifyAmount(productId, amount);
        System.out.println(FRAME_COLORSCHEME + " A " + productId + " cikkszámú termékből " + (-1 * amount) + " darabot betettünk a kosárba." + LINE_INPUT_COLORSCHEME);
    }

    private void deleteFromCart() {
        printCart();
        Scanner scanner = new Scanner(System.in);
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Törölni kívánt termék cikkszáma: ");
        long productId = Integer.parseInt(scanner.nextLine());
        shopService.removeItem(productId);
        System.out.println(FRAME_COLORSCHEME + " A " + productId + " cikkszámú terméket töröltük a kosárból." + LINE_INPUT_COLORSCHEME);
    }

    private void toCart() {
        printCart();
        printProducts();
        Scanner scanner = new Scanner(System.in);
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Megvenni kívánt termék cikkszáma: ");
        long productId = Integer.parseInt(scanner.nextLine());
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Mennyisége ");
        int amount = Integer.parseInt(scanner.nextLine());
        shopService.addItem(productId, amount);
        System.out.println(FRAME_COLORSCHEME + " A " + productId + " cikkszámú termékből " + amount + " darabot betettünk a kosárba." + LINE_INPUT_COLORSCHEME);

    }

    private void printCart() {
        List<String> cartRows = new ArrayList<>();
        int sum = 0;
        for (Item actual : shopService.getUserCart().contentOfCart()) {
//            String temp = String.format("Cikkszám: %-6s  ", actual.getProduct().getId()) + actual.getProduct().getName();
//            String formattedLine = String.format("%-39s", temp) + String.format("%6s Ft", actual.getProduct().getPrice()) +
//            String.format("%6s db %8s Ft", actual.getAmount(), actual.getSumPrice());

            String temp = String.format("Cikkszám: %4s %-29s  %6s Ft   %4s db   %8s Ft", actual.getProduct().getId(), actual.getProduct().getName(),
            actual.getProduct().getPrice(), actual.getAmount(), actual.getSumPrice());

            cartRows.add(printToConsole.upToWidth("     " + temp, 88));
            sum += actual.getSumPrice();
        }
        cartRows.add(printToConsole.upToWidth("      " + "", 88));
        String sumString = "A kosár összértéke: " + sum + " Ft";
        cartRows.add(String.format("%84s  ", sumString));

        printToConsole.printRows("A kosár aktuális tartalma", null, cartRows, 88);
    }

    private void printProducts() {
        System.out.println();
        List<String> productRows = new ArrayList<>();
        for (Product actual : shopService.getProductList()) {
            String temp = String.format("Cikkszám: %-6s  ", actual.getId()) + actual.getName();
            String formattedLine = String.format("%-39s", temp) + String.format("%6d Ft", actual.getPrice());
            productRows.add(printToConsole.upToWidth("      " + formattedLine, 62));
        }
        printToConsole.printRows("Webshopunk termékei", null, productRows, 62);
    }

    private void runLoginMenu() {
        boolean exit = false;
        do {
            printMenu("Belépés csak regisztrált ügyfeleknek", MENUITEMS_LOGIN);
            switch (getSelectedMenuItem(3)) {
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
        System.out.println(FRAME_COLORSCHEME + " Kilépés " + LINE_INPUT_COLORSCHEME);
    }

    private void registerUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(FRAME_COLORSCHEME + " Regisztráció " + LINE_INPUT_COLORSCHEME);
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Felhasználónév: ");
        String userName = scanner.nextLine();
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Jelszó: ");
        String password = scanner.nextLine();
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Email: ");
        String email = scanner.nextLine();
        shopService.registerUser(userName, password, email);
    }

    public void Login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(FRAME_COLORSCHEME + " Belépés " + LINE_INPUT_COLORSCHEME);
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Felhasználónév: ");
        String userName = scanner.nextLine();
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Jelszó: ");
        String password = scanner.nextLine();
        shopService.logIn(userName, password);
        runShoppingMenu();
    }


    public int getSelectedMenuItem(int menuLinesNumber) {
        Scanner scanner = new Scanner(System.in);
        int selectedMenuItem = 0;
        do {
            System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Válassz a menüből! ");
            String s = scanner.nextLine();
            try {
                selectedMenuItem = Integer.parseInt(s);
            } catch (IllegalArgumentException iae) {
                selectedMenuItem = 0;
            } finally {
                if (selectedMenuItem <= 0 || selectedMenuItem > menuLinesNumber) {
                    System.out.println(FRAME_COLORSCHEME + " Nincs ilyen menüpont! " + LINE_INPUT_COLORSCHEME);
                }
            }
        } while (selectedMenuItem <= 0 || selectedMenuItem > menuLinesNumber);
        return selectedMenuItem;
    }

    public void printMenu(String heading, List<String> menuItems) {
        List<String> printMenuItems = new ArrayList<>();
        System.out.println();
        for (String actual : menuItems) {
            printMenuItems.add(printToConsole.centerText(actual, 68));
        }
        printToConsole.printRows("M E N Ü", heading, printMenuItems, 70);
    }

}

