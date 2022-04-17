package webshop;

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
        System.out.println();
        printHeader();
        printBody();
        printFooter();
    }

    private void printHeader() {
        printSolidLine();
        for (String actual : headings) {
            frameAndHeadingPrint(actual);
            printSolidLine();
        }
    }

    private void printBody() {
        for (int i = 0; i <= lines.size() - 1; i++) {
            if (i % 2 == 0) {
                frameAndEvenPrint(lines.get(i));
            } else {
                frameAndOddPrint(lines.get(i));
            }
        }
    }

    private void printFooter() {
        printSolidLine();
    }

    private String centerText(String text) {
        String spaces = " ".repeat((width - (width % 2) - text.length()) / 2);
        return text.length() % 2 == 0 ? spaces + text + spaces : spaces + " " + text + spaces;
    }

    private void frameAndOddPrint(String text) {
        System.out.println(FRAME_COLORSCHEME + " "+ ROW_ODD_COLORSCHEME + centerText(text) + FRAME_COLORSCHEME+" " + DEFAULT_COLORSCHEME);
    }

    private void frameAndEvenPrint(String text) {
        System.out.println(FRAME_COLORSCHEME + " "+ ROW_EVEN_COLORSCHEME + centerText(text) + FRAME_COLORSCHEME+" " + DEFAULT_COLORSCHEME);
    }

    private void printSolidLine() {
        frameAndHeadingPrint("");
    }

    private void frameAndHeadingPrint(String text) {
        System.out.println(FRAME_COLORSCHEME + " " + centerText(text) + " " + DEFAULT_COLORSCHEME);
    }
}

