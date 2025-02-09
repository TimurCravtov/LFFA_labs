package md.utm.lab1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LetterListHelper {

    public static List<Letter> getLetterListFromString(String s) {
        List<Letter> letterList = new ArrayList<>();

        for (char ch : s.toCharArray()) {
            letterList.add(new Letter(Character.toString(ch)));
        }
        return letterList;
    }

    public static List<Letter> getLetterListFromStrings(String ... s) {

        return new ArrayList<>(Arrays.stream(s).map(Letter::new).toList());

    }
}


