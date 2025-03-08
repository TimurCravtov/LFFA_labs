package md.utm.utils;

public class ColorManager {
    public static final String RESET = "\033[0m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";
    public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
    public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE

    public static String colorize(String input, String color) {
        return String.format("%s%s%s", color, input, RESET);
    }
}
