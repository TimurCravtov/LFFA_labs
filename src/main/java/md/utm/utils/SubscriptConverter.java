package md.utm.utils;

public class SubscriptConverter {
    public static String toSubscript(int num) {
        String numStr = String.valueOf(num);

        StringBuilder subscriptStr = new StringBuilder();
        String[] subscriptDigits = {"₀", "₁", "₂", "₃", "₄", "₅", "₆", "₇", "₈", "₉"};

        for (char c : numStr.toCharArray()) {
            if (Character.isDigit(c)) {
                subscriptStr.append(subscriptDigits[c - '0']);
            } else {
                subscriptStr.append(c);
            }
        }

        return subscriptStr.toString();
    }
}