package md.utm.lab3;

import md.utm.utils.FileReader;

import java.io.File;
import java.util.*;

public class Lexer {
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
            "import", "as", "calendar", "event", "default", "until", "from",
            "if", "include", "in", "export", "local", "settings"
    ));

    private static final Set<String> DAYS_OF_WEEK = new HashSet<>(Arrays.asList(
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    ));

    public static List<Token> tokenize(String input) {
        input = input.replaceAll("//.*", ""); // Remove comments
        List<Token> tokens = new ArrayList<>();
        int pos = 0;

        while (pos < input.length()) {
            char current = input.charAt(pos);

            if (Character.isWhitespace(current)) {
                pos++;
                continue;
            }

            if (current == '"') {
                pos++;
                StringBuilder sb = new StringBuilder();
                while (pos < input.length() && input.charAt(pos) != '"') {
                    sb.append(input.charAt(pos));
                    pos++;
                }
                pos++;
                tokens.add(new Token(TokenType.STRING, sb.toString()));
                continue;
            }

            if (Character.isLetter(current) || current == '_') {
                int start = pos;
                while (pos < input.length() &&
                        (Character.isLetterOrDigit(input.charAt(pos)) || input.charAt(pos) == '_')) {
                    pos++;
                }
                String word = input.substring(start, pos);

                if (KEYWORDS.contains(word)) {
                    tokens.add(new Token(TokenType.KEYWORD, word));
                } else if (DAYS_OF_WEEK.contains(word)) {
                    tokens.add(new Token(TokenType.DAY_OF_WEEK, word));
                } else {
                    tokens.add(new Token(TokenType.VARIABLE, word));
                }
                continue;
            }

            if (Character.isDigit(current)) {
                int start = pos;
                while (pos < input.length() &&
                        (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '-' || input.charAt(pos) == ':')) {
                    pos++;
                }
                String tokenStr = input.substring(start, pos);
                if (tokenStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    tokens.add(new Token(TokenType.DATE, tokenStr));
                } else if (tokenStr.matches("\\d{1,2}:\\d{2}")) {
                    tokens.add(new Token(TokenType.TIME, tokenStr));
                } else {
                    tokens.add(new Token(TokenType.NUMBER, tokenStr));
                }
                continue;
            }

            if (current == '=') {
                if (pos + 1 < input.length() && input.charAt(pos + 1) == '=') {
                    tokens.add(new Token(TokenType.OPERATOR, "=="));
                    pos += 2;
                } else {
                    tokens.add(new Token(TokenType.SYMBOL, "="));
                    pos++;
                }
                continue;
            }

            if (";:(),{}.".indexOf(current) != -1 || current == ',') {
                tokens.add(new Token(TokenType.SYMBOL, Character.toString(current)));
                pos++;
                continue;
            }

            pos++;
        }
        return tokens;
    }

    public static void main(String[] args) {
        String sampleInput = FileReader.read("D:\\src\\utm\\tasks\\lfa\\labs\\src\\main\\java\\md\\utm\\lab3\\resources\\sample.aion");

        List<Token> tokens = tokenize(sampleInput);
        tokens.forEach(System.out::println);
    }
}
