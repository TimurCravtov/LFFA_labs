package md.utm.tokenizer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import md.utm.utils.ContainsIgnoreCaseKt;

public class AionLexer {

    private static final Set<String> KEYWORDS = Set.of(
            "import", "as", "calendar", "event", "default", "until", "from",
            "if", "include", "in", "export", "local", "settings", "new", "pomodoro", "step",
            "iterate", "find", "filter", "merge", "where", "into", "weeknumber", "for", "times",
            "with", "pause", "between", "and", "on", "every", "at", "repeat", "task", "days", "weeks", "months", "years"
    );

    private static final Set<String> REPEAT_CONDITIONS = Set.of("daily", "weekly", "monthly", "yearly");

    private static final Map<String, Set<String>> DAYS_OF_WEEK_VARIATIONS = Map.of(
            "Monday", Set.of("Monday", "Mon", "M"),
            "Tuesday", Set.of("Tuesday", "Tue", "Tu"),
            "Wednesday", Set.of("Wednesday", "Wed", "W"),
            "Thursday", Set.of("Thursday", "Thu", "Th"),
            "Friday", Set.of("Friday", "Fri", "F"),
            "Saturday", Set.of("Saturday", "Sat", "Sa"),
            "Sunday", Set.of("Sunday", "Sun", "Su")
    );

    private static final Map<String, Set<String>> MONTHS_VARIATIONS = new HashMap<>() {{
        put("January", Set.of("January", "Jan"));
        put("February", Set.of("February", "Feb"));
        put("March", Set.of("March", "Mar"));
        put("April", Set.of("April", "Apr"));
        put("May", Set.of("May"));
        put("June", Set.of("June", "Jun"));
        put("July", Set.of("July", "Jul"));
        put("August", Set.of("August", "Aug"));
        put("September", Set.of("September", "Sep"));
        put("October", Set.of("October", "Oct"));
        put("November", Set.of("November", "Nov"));
        put("December", Set.of("December", "Dec"));
    }};

    private static final Set<String> COMPARISON_OPERATORS = Set.of("==", "!=", "><", ">", "<", ">=", "<=");
    private static final Pattern TIME_PATTERN = Pattern.compile("\\d{1,2}:\\d{2}(?=[^a-zA-Z0-9_]|$)");
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}.\\d{1,2}.\\d{1,2}(?=[^a-zA-Z0-9_]|$)");

    private static final Pattern STRING_PATTERN = Pattern.compile("\"([^\"]*)\"(?=[^a-zA-Z0-9_]|$)");
    private static final Pattern PERIOD_PATTERN = Pattern.compile("\\d+([hmdwy])(?=[^a-zA-Z0-9_]|$)");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+(?=[^a-zA-Z0-9_]|$)");
;
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*(?=[^a-zA-Z0-9_]|$)");


    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int pos = 0;


        // 1. Remove comments
        input = input.replaceAll("//.*", "");

        while (pos < input.length()) {
            char current = input.charAt(pos);

            if (Character.isWhitespace(current)) {
                pos++;
                continue;
            }

            // Extract strings
            Matcher stringMatcher = STRING_PATTERN.matcher(input.substring(pos));
            if (stringMatcher.lookingAt()) {
                tokens.add(new Token(TokenType.STRING, stringMatcher.group(1), pos));
                pos += stringMatcher.group().length();
                continue;
            }

            // Extract time in form 21:00
            Matcher timeMatcher = TIME_PATTERN.matcher(input.substring(pos));
            if (timeMatcher.lookingAt()) {
                tokens.add(new Token(TokenType.TIME, timeMatcher.group(), pos));
                pos += timeMatcher.group().length();
                continue;
            }

            // Extract period in form 2h
            Matcher periodMatcher = PERIOD_PATTERN.matcher(input.substring(pos));
            if (periodMatcher.lookingAt()) {
                tokens.add(new Token(TokenType.PERIOD, periodMatcher.group(), pos));
                pos += periodMatcher.group().length();
                continue;
            }


            // extract date like 2025.10.20
            Matcher dataMatcher = DATE_PATTERN.matcher(input.substring(pos));
            if (dataMatcher.lookingAt()) {
                tokens.add(new Token(TokenType.DATE, dataMatcher.group(), pos));
                pos += dataMatcher.group().length();
                continue;
            }

            // Simply extract number
            Matcher numberMatcher = NUMBER_PATTERN.matcher(input.substring(pos));
            if (numberMatcher.lookingAt()) {
                tokens.add(new Token(TokenType.NUMBER, numberMatcher.group(), pos));
                pos += numberMatcher.group().length();
                continue;
            }


            // Extract operators of comparison
            String finalInput = input;
            int finalPos = pos;
            Optional<String> matchedOp = COMPARISON_OPERATORS.stream()
                    .filter(op -> finalInput.startsWith(op, finalPos))
                    .findFirst();
            if (matchedOp.isPresent()) {
                tokens.add(new Token(TokenType.COMPARISON_CONDITION, matchedOp.get(), pos));
                pos += matchedOp.get().length();
                continue;
            }


            // Extract sequence of alfanumeric
            Matcher varMatcher = VARIABLE_PATTERN.matcher(input.substring(pos));
            if (varMatcher.lookingAt()) {
                String word = varMatcher.group();

                // if word is keyword
                if (KEYWORDS.contains(word)) {
                    tokens.add(new Token(TokenType.KEYWORD, word, pos));

                // if word is day of week
                } else if (DAYS_OF_WEEK_VARIATIONS.values().stream().anyMatch(set -> ContainsIgnoreCaseKt.containsIgnoreCase(set, word))) {
                    tokens.add(new Token(TokenType.DAY_OF_WEEK, word, pos));

                // if word is month
                } else if (MONTHS_VARIATIONS.values().stream().anyMatch(set -> ContainsIgnoreCaseKt.containsIgnoreCase(set, word))) {
                    tokens.add(new Token(TokenType.MONTH, word, pos));

                // if word is repeaters words (like daily, monthly, etc.)
                } else if (REPEAT_CONDITIONS.contains(word.toLowerCase())) {
                    tokens.add(new Token(TokenType.REPEAT_CONDITION, word, pos));
                } else {
                    tokens.add(new Token(TokenType.VARIABLE, word, pos));
                }
                pos += word.length();
                continue;
            }

            if (current == '\n') {
                tokens.add(new Token(TokenType.DELIMITER, "\\n", pos));
                pos++;
                continue;
            } else if (current == ';') {
                tokens.add(new Token(TokenType.DELIMITER, ";", pos));
                pos++;
                continue;
            }

            if (current == '=') {
                tokens.add(new Token(TokenType.ASSIGMENT_OPERATOR, "=", pos));
                pos++;
                continue;
            }
            // Symbols
            if ("{}(),+:".indexOf(current) != -1) {
                tokens.add(new Token(TokenType.SYMBOL, String.valueOf(current), pos));
                pos++;
                continue;
            }

            throw new RuntimeException("Unexpected token: " + current + " at position " + pos);
        }

        return tokens;
    }
}
