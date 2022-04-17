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
    private List<String> lines;
    private List<String> headings;

    public TableForUX(int width, List<String> lines, List<String> headings) {
        this.width = width;
        this.lines = lines;
        this.headings = headings;
    }

    public void print() {
        printHeader();
        printBody();
        printFooter();
    }

    private void printHeader() {
        System.out.println();
        printSolidLine();
        for (String actual : headings) {
            frameAndTextPrint(centerText(actual));
            printSolidLine();
        }
    }

    private void printBody() {
        for (int i = 0; i <= lines.size() - 1; i++) {
            if (i % 2 == 0) {
                frameAndTextPrint(ROW_EVEN_COLORSCHEME + centerText(lines.get(i)) + FRAME_COLORSCHEME);
            } else {
                frameAndTextPrint(ROW_ODD_COLORSCHEME + centerText(lines.get(i)) + FRAME_COLORSCHEME);
            }
        }
    }

    private void printFooter() {
        printSolidLine();
    }

    private String centerText(String text) {
        StringBuilder resultSB = new StringBuilder();
        String spaces = resultSB.append(" ".repeat((width - text.length()) / 2)).toString();
        return text.length() % 2 == 0 ? spaces + text + spaces : spaces + " " + text + spaces;
    }

    private void printSolidLine() {
        frameAndTextPrint(centerText(""));
    }

    private void frameAndTextPrint(String text) {
        System.out.println(FRAME_COLORSCHEME + " " + text + " " + DEFAULT_COLORSCHEME);
    }
}

