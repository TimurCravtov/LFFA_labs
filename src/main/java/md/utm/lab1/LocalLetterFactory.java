package md.utm.lab1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalLetterFactory {
    private final Map<String, Letter> letters = new HashMap<>();

    public Letter create(String name, boolean isTerminal) {
        return letters.computeIfAbsent(name, k -> new Letter(name, isTerminal));
    }

    public Letter get(String name) {

        Letter l = letters.get(name);
        if (l == null) {
            throw new RuntimeException(STR."Letter \{name} does not exist");
        }
        return l;
    }

    public List<Letter> getLetterListFromString(String s) {
        List<Letter> letterList = new ArrayList<>();

        for (char ch : s.toCharArray()) {
            letterList.add(get(String.valueOf(ch)));
        }
        return letterList;
    }
}
