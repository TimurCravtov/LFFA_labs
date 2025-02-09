package md.utm.lab1;

import java.util.Objects;

public class Letter implements State, AlphabetSymbol {

//    public static final Letter epsilon = new Letter("ε", true);

    public static final Letter epsilon = new Letter("");
    public static final Letter F = new Letter("{final}");

    private final String letter;

    public Letter(String letter) {
        this.letter = letter;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Letter letter1 = (Letter) obj;
        return Objects.equals(letter, letter1.letter);
    }

    public String getLetter() {
        return letter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter);
    }

    public String toString() {
        return letter;
    }

    @Override
    public String getStateName() {
        return getLetter();
    }

    @Override
    public String getAlphabetSymbolName() {
        return getLetter();
    }
}

