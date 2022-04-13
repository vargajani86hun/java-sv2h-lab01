package webshop;

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
            "[1] Termékek listázása",
            "[2] Termék kosárba helyezése",
            "[3] Termék kivétele a kosárból",
            "[4] Termék mennyiségének növelése",
            "[5] Termék mennyiségének csökkentése",
            "[6] Rendelés véglegesítése",
            "[7] Kilépés");

    private PrintToConsole printToConsole;
    private Cart cart;

    public static void main(String[] args) {
        WebShopMain webShopMain = new WebShopMain();
        webShopMain.printToConsole = new PrintToConsole();
        webShopMain.cart = new Cart();
        webShopMain.runLoginMenu();
        webShopMain.runShoppingMenu();
    }

    private void runShoppingMenu() {
        boolean exit = false;
        do {
            printMenu(null, MENUITEMS_WEBSHOP);
            switch (getSelectedMenuItem(7)) {
                case 1:
                    listProducts();
                    break;
                case 2:
                    toCart();
                    break;
                case 3:
                    deleteFromCart();
                    break;
                case 4:
                    increaseAmount();
                    break;
                case 5:
                    decreaseAmount();
                    break;
                case 6:
                    finalOrder();
                    break;
                case 7:
                    exit = true;
            }
        } while (!exit);
    }

    private void finalOrder() {
        Scanner scanner = new Scanner(System.in);
        List<String> cartRows = new ArrayList<>();
        for (String actual : List.of("Kalapács","Fúró","Lapát","Csavar")) {
            cartRows.add(printToConsole.upToWidth("        "+actual, 50));
        }
        printToConsole.printRows("A kosár tartalma", null, cartRows, 50);
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Véglegesíted a rendelést? (i/n) ");
        String answer = scanner.nextLine();
    }

    private void decreaseAmount() {
        Scanner scanner = new Scanner(System.in);
        List<String> cartRows = new ArrayList<>();
        for (String actual : List.of("Kalapács","Fúró","Lapát","Csavar")) {
            cartRows.add(printToConsole.upToWidth("        "+actual, 50));
        }
        printToConsole.printRows("A kosár tartalma", null, cartRows, 50);
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Melyk cikkszámú termék mennyiségét csökkentenéd? ");
        String name = scanner.nextLine();
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Mennyiség: ");
        int amount = Integer.parseInt(scanner.nextLine());
    }

    private void increaseAmount() {
        Scanner scanner = new Scanner(System.in);
        List<String> cartRows = new ArrayList<>();
        for (String actual : List.of("Kalapács","Fúró","Lapát","Csavar")) {
            cartRows.add(printToConsole.upToWidth("        "+actual, 50));
        }
        printToConsole.printRows("A kosár tartalma", null, cartRows, 50);
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Melyk cikkszámú termék mennyiségét növelnéd? ");
        String name = scanner.nextLine();
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Mennyiség: ");
        int amount = Integer.parseInt(scanner.nextLine());
    }

    private void deleteFromCart() {
        Scanner scanner = new Scanner(System.in);
        List<String> cartRows = new ArrayList<>();
        for (String actual : List.of("Kalapács","Fúró","Lapát","Csavar")) {
            cartRows.add(printToConsole.upToWidth("        "+actual, 50));
        }
        printToConsole.printRows("A kosár tartalma", null, cartRows, 50);
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Törölni kívánt termék cikkszáma: ");
        String name = scanner.nextLine();
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Mennyisége ");
        int amount = Integer.parseInt(scanner.nextLine());
    }

    private void toCart() {
        Scanner scanner = new Scanner(System.in);
        List<String> cartRows = new ArrayList<>();
        for (String actual : cart.contentOfCart()) {
            cartRows.add(printToConsole.upToWidth("        "+actual, 50));
        }
        printToConsole.printRows("A kosár tartalma", null, cartRows, 50);
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Megvenni kívánt termék cikkszáma: ");
        String name = scanner.nextLine();
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Mennyisége ");
        int amount = Integer.parseInt(scanner.nextLine());

    }

    private void listProducts() {
        List<String> productRows = new ArrayList<>();
        for (String actual : List.of("Kalapács","Fúró","Lapát","Csavar")) {
            productRows.add(printToConsole.upToWidth("        "+actual, 50));
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
                    Register();
                    break;
                case 3:
                    exit = true;
            }
        } while (!exit);
    }

    private void Register() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Felhasználónév: ");
        String userName = scanner.nextLine();
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Jelszó: ");
        String password = scanner.nextLine();
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Email: ");
        String email = scanner.nextLine();
//        shopService.registerUser(userName, password, email);
    }

    public void Login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Felhasználónév: ");
        String userName = scanner.nextLine();
        System.out.print(FRAME_COLORSCHEME + " " + LINE_INPUT_COLORSCHEME + " Jelszó: ");
        String password = scanner.nextLine();
//        shopService.logIn(userName, password);
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
