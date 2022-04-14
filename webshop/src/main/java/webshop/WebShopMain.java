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
    private Cart cart;
    private ShopService shopService;

    public static void main(String[] args) {
        WebShopMain webShopMain = new WebShopMain();
        webShopMain.printToConsole = new PrintToConsole();
        webShopMain.cart = new Cart();

        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/webshop?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("klaradb");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot reach DataBase!", sqle);
        }

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
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
            System.out.println(FRAME_COLORSCHEME + " Köszönjük a megrendelést, iratkozzon fel a hírlevélre, elárasztjuk spammekkel! " + LINE_INPUT_COLORSCHEME);
            // rendelés feladása
            // kosár törlése vagy kilépés
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
        System.out.println(FRAME_COLORSCHEME + " A "+productId+" cikkszámú termékből "+ (-1*amount) +" darabot kivettünk a kosárból." + LINE_INPUT_COLORSCHEME);
    }

    private void increaseAmount() {
        printCart();
        Scanner scanner = new Scanner(System.in);
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Melyk cikkszámú termék mennyiségét növelnéd? ");
        long productId = Integer.parseInt(scanner.nextLine());
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Mennyiség: ");
        int amount = Integer.parseInt(scanner.nextLine());
        shopService.modifyAmount(productId, amount);
        System.out.println(FRAME_COLORSCHEME + " A "+productId+" cikkszámú termékből "+ (-1*amount) +" darabot betettünk a kosárba." + LINE_INPUT_COLORSCHEME);
    }

    private void deleteFromCart() {
        printCart();
        Scanner scanner = new Scanner(System.in);
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Törölni kívánt termék cikkszáma: ");
        long productId = Integer.parseInt(scanner.nextLine());
        shopService.removeItem(productId);
        System.out.println(FRAME_COLORSCHEME + " A "+productId+" cikkszámú terméket töröltük a kosárból." + LINE_INPUT_COLORSCHEME);
    }

    private void toCart() {
        printCart();
        Scanner scanner = new Scanner(System.in);
        printProducts();
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Megvenni kívánt termék cikkszáma: ");
        long productId = Integer.parseInt(scanner.nextLine());
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Mennyisége ");
        int amount = Integer.parseInt(scanner.nextLine());
        shopService.modifyAmount(productId, amount);
        System.out.println(FRAME_COLORSCHEME + " A "+productId+" cikkszámú termékből "+ amount +" darabot betettünk a kosárba." + LINE_INPUT_COLORSCHEME);
    }

    private List<String> formatText() {
        List<String> result = new ArrayList<>();
        int a = 3;
        int b = 18;
        int c = 2231;
        int d = 23;
        int a2 = 35;
        int b2 = 5;
        int c2 = 2821;
        int d2 = 9337;
        String a1 = String.format("Cikkszám: %-6s  ", a) + " Ár";
        String b1 = String.format("Cikkszám: %-6s  ", b) + " Csavarhúzó";
        String c1 = String.format("Cikkszám: %-6s  ", c) + " Harapófogó";
        String d1 = String.format("Cikkszám: %-6s  ", d) + " Gyémánt vágótárcsa";

        result.add(String.format("%-39s", a1) + String.format("%6d db", a2));
        result.add(String.format("%-39s", b1) + String.format("%6d db", b2));
        result.add(String.format("%-39s", c1) + String.format("%6s db", c2));
        result.add(String.format("%-39s", d1) + String.format("%6s db", d2));
        String sum= "A kosár összértéke: "+(a2+b2+c2+d2)+" db";
        result.add("");
        result.add(String.format("%48s", sum));

        return result;
    }

    private void printCart() {
        List<String> cartRows = new ArrayList<>();
        for (String actual : formatText()) {
            cartRows.add(printToConsole.upToWidth("       " + actual, 64));
        }

//        List<String> cartRows = new ArrayList<>();
//        for (String actual : cart.contentOfCart()) {
//            cartRows.add(printToConsole.upToWidth("        " + actual, 50));
//        }
        printToConsole.printRows("A kosár aktuális tartalma", null, cartRows, 64);
    }

    private void printProducts() {
        List<String> productRows = new ArrayList<>();
        for (String actual : shopService.getProductList()) {
            productRows.add(printToConsole.upToWidth("        " + actual, 50));
        }
        printToConsole.printRows("Webshopunk termékei", null, productRows, 50);
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
//        shopService.logIn(userName, password);
        runShoppingMenu();
    }


    public int getSelectedMenuItem(int menuLines) {
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
                if (selectedMenuItem <= 0 || selectedMenuItem > menuLines) {
                    System.out.println(FRAME_COLORSCHEME + " Nincs ilyen menüpont! " + LINE_INPUT_COLORSCHEME);
                }
            }
        } while (selectedMenuItem <= 0 || selectedMenuItem > menuLines);
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

