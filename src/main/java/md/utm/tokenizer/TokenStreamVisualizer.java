package md.utm.tokenizer;

import java.util.List;

import static md.utm.utils.ColorManager.*;
import static md.utm.utils.ColorManager.colorize;

public class TokenStreamVisualizer {

    public static void visualize(List<Token> tokens) {
        StringBuilder firstLine = new StringBuilder();
        StringBuilder secondLine = new StringBuilder();

        for (Token token : tokens) {

            String type = String.valueOf(token.getType());
            String value = token.getValue();

            int width = Math.max(type.length(), value.length()) + 2;

            String colouredType = switch (TokenType.valueOf(type)) {
                case KEYWORD -> colorize(type, BLUE);
                case STRING -> colorize(type, GREEN);
                case DATE -> colorize(type, RED);
                case REPEAT_CONDITION -> colorize(type, YELLOW);
                case TIME -> colorize(type, PURPLE);
                case PERIOD -> colorize(type, CYAN);
                case VARIABLE -> colorize(type, YELLOW_BRIGHT);
                case ASSIGMENT_OPERATOR -> colorize(type, CYAN_BRIGHT);
                case MONTH -> colorize(type, RED_BRIGHT);
                case NUMBER -> colorize(type, GREEN_BRIGHT);

                default -> type;
            };

            String formattedType = String.format("%s" + " ".repeat(width - type.length()), colouredType);
            String formattedValue = String.format("%s" + " ".repeat(width - value.length()) , value);

            firstLine.append(formattedType);
            secondLine.append(formattedValue);
        }

        System.out.println(firstLine);
        System.out.println(secondLine);
    }
}
