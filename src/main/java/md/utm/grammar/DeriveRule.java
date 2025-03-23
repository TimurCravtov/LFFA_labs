package md.utm.grammar;

import java.util.List;

public class DeriveRule {
    private final List<Letter> from;
    private final List<Letter> to;

    public DeriveRule(List<Letter> from, List<Letter> to) {
        this.from = List.copyOf(from);
        this.to = List.copyOf(to);
    }

    @Override
    public int hashCode() {
        return from.hashCode() + to.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DeriveRule other) {
            return from.equals(other.from) && to.equals(other.to);
        }
        return false;
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
