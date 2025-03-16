package md.utm.grammar;

import java.util.List;
import java.util.stream.Collectors;

public class Word {

    public static String makeString(List<Letter> word) {
        return word.stream()
                .map(Letter::toString)
                .collect(Collectors.joining());
    }


}