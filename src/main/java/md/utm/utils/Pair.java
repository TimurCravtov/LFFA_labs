package md.utm.utils;

public record Pair<T1, T2>(T1 value1, T2 value2) {
    public String toString() {
        return String.format("<%s, %s>", value1, value2);
    }

    @Override
    public int hashCode() {
        return value1.hashCode() + value2.hashCode();
    }
}