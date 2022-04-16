package webshop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableForUX {
    private static final String DEFAULT_COLORSCHEME = "\u001B[39;49m";
    private static final String FRAME_COLORSCHEME = "\u001B[30;43m";
    private static final String ROW_EVEN_COLORSCHEME = "\u001B[39;49m";
    private static final String ROW_ODD_COLORSCHEME = "\u001B[30;47m";

    private int width;
    private List<String> lines = new ArrayList<>();
    private List<String> headings = new ArrayList<>();

    public TableForUX(int width, List<String> lines, List<String> headings) {
        this.width = width;
        this.lines = lines;
        this.headings = headings;
    }

    public void print() {
        System.out.println();
        printHeader();
        for (int i = 0; i <= lines.size() - 1; i++) {
            if (i % 2 == 0) {
                System.out.println(FRAME_COLORSCHEME + " " + ROW_EVEN_COLORSCHEME + centerText(lines.get(i)) + FRAME_COLORSCHEME + " " + DEFAULT_COLORSCHEME);
            } else {
                System.out.println(FRAME_COLORSCHEME + " " + ROW_ODD_COLORSCHEME + centerText(lines.get(i)) + FRAME_COLORSCHEME + " " + DEFAULT_COLORSCHEME);
            }
        }
        printFooter();
    }

    private String centerText(String textToCenter) {
        StringBuilder resultSB = textToCenter.length() % 2 != 0 ? new StringBuilder(" "+ textToCenter): new StringBuilder(textToCenter);
        int padding = (width - textToCenter.length()) / 2;
        resultSB.insert(0, " ".repeat(padding));
        resultSB.append(" ".repeat(padding));
        return resultSB.toString();
    }

    private void printHeader() {
        System.out.println(FRAME_COLORSCHEME +" "+ centerText("") +" "+ DEFAULT_COLORSCHEME);
        for (String actual: headings) {
            System.out.println(FRAME_COLORSCHEME +" "+ centerText(actual) +" "+ DEFAULT_COLORSCHEME);
            System.out.println(FRAME_COLORSCHEME +" "+ centerText("") +" "+ DEFAULT_COLORSCHEME);
        }
    }

    private void printFooter() {
        System.out.println(FRAME_COLORSCHEME +" "+ centerText("") +" "+ DEFAULT_COLORSCHEME);
    }

    public static void main(String[] args) {
        List<String> rows= new ArrayList<>(Arrays.asList("Egy","hatvanhat","Három"));
        List<String> heads= new ArrayList<>(Arrays.asList("ez itt az első head","ez itt az második első head","ez itt a harmadik első head"));
        TableForUX tableForUX = new TableForUX(80,rows,heads);
        tableForUX.print();
    }
}

