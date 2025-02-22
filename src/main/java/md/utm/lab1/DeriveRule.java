package md.utm.lab1;

import java.util.List;

public class DeriveRule {
    private final List<Letter> from;
    private final List<Letter> to;

    public DeriveRule(List<Letter> from, List<Letter> to) {
        this.from = List.copyOf(from);
        this.to = List.copyOf(to);
    }

    public DeriveRule(Letter from, Letter to) {
        this.from = List.of(from);
        this.to = List.of(to);
    }

    public DeriveRule(Letter from, List<Letter> to) {
        this.from = List.of(from);
        this.to = List.copyOf(to);
    }

    @Override
    public String toString() {

        String firstPart = Word.makeString(from);
        String secondPart = Word.makeString(to);

        return String.format("%s → %s", firstPart, secondPart);
    }

    public DeriveRule(List<Letter> from, Letter to) {
        this.from = List.copyOf(from);
        this.to = List.of(to);
    }

    public List<Letter> getFrom() {
        return from;
    }

    public List<Letter> getTo() {
        return to;
    }
}
