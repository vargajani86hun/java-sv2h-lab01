package webshop;

import java.util.List;

public class PrintToConsole {
    private static final String DEFAULT_COLORSCHEME = "\u001B[39;49m";
    private static final String FRAME_COLORSCHEME = "\u001B[30;43m";
    private static final String ROW_EVEN_COLORSCHEME = "\u001B[39;49m";
    private static final String ROW_ODD_COLORSCHEME = "\u001B[30;47m";


    public String upToWidth(String line, int width) {
        StringBuilder sb = new StringBuilder(line);
        sb.append(" ".repeat(width - line.length() - 2));
        return sb.toString();
    }

    public String centerText(String textToCenter, int width) {
        StringBuilder resultSB = new StringBuilder(textToCenter);
        if (resultSB.length() % 2 != 0) {
            resultSB.append(" ");
        }
        int padding = (width - textToCenter.length()) / 2;
        resultSB.insert(0, " ".repeat(padding));
        resultSB.append(" ".repeat(padding));
        return resultSB.toString();
    }

    public void printRows(String heading, String heading2, List<String> lines, int width) {
        printHeading(heading, heading2, width);
        for (int i = 0; i <= lines.size() - 1; i++) {
            if (i % 2 == 0) {
                System.out.println(FRAME_COLORSCHEME + " " + ROW_EVEN_COLORSCHEME + lines.get(i) + FRAME_COLORSCHEME + " " + DEFAULT_COLORSCHEME);
            } else {
                System.out.println(FRAME_COLORSCHEME + " " + ROW_ODD_COLORSCHEME + lines.get(i) + FRAME_COLORSCHEME + " " + DEFAULT_COLORSCHEME);
            }
        }
        printFooter(width);
    }

    public void printRow(String heading, String line, int width) {
        printHeading(heading, null, width);
        System.out.println(FRAME_COLORSCHEME + " " + ROW_EVEN_COLORSCHEME + line + FRAME_COLORSCHEME + " " + DEFAULT_COLORSCHEME);
        printFooter(width);
    }

    public void printHeading(String heading, String heading2, int width) {
        System.out.println(FRAME_COLORSCHEME + centerText("", width) + DEFAULT_COLORSCHEME);
        if (heading2 != null) {
            System.out.println(FRAME_COLORSCHEME + centerText(heading2, width) + DEFAULT_COLORSCHEME);
            System.out.println(FRAME_COLORSCHEME + centerText("", width) + DEFAULT_COLORSCHEME);
        }
        System.out.println(FRAME_COLORSCHEME + centerText(heading, width) + DEFAULT_COLORSCHEME);
        System.out.println(FRAME_COLORSCHEME + centerText("", width) + DEFAULT_COLORSCHEME);
    }

    public void printFooter(int width) {
        System.out.println(FRAME_COLORSCHEME + centerText("", width) + DEFAULT_COLORSCHEME);
    }
}
