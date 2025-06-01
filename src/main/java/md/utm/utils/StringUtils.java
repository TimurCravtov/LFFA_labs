package md.utm.utils;

import java.util.Set;

public class StringUtils {
    public static boolean containsIgnoreCase(Set<String> set, String element) {
        for (String item : set) {
            if (item.equalsIgnoreCase(element)) {
                return true;
            }
        }
        return false;
    }
}
