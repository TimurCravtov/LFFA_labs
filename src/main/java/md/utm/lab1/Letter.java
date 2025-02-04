package md.utm.lab1;

import java.util.Objects;

public class Letter {
    private final String letter;
    private final boolean isTerminal;

    public Letter(String letter, boolean isTerminal) {
        this.letter = letter;
        this.isTerminal = isTerminal;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Letter letter1 = (Letter) obj;
        return isTerminal == letter1.isTerminal && Objects.equals(letter, letter1.letter);
    }

    public boolean isTerminal() {
        return isTerminal;
    }
    public String getLetter() {
        return letter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, isTerminal);
    }

    public String toString() {
        return letter;
    }
}
