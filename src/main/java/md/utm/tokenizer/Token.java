package md.utm.tokenizer;

public class Token {
    private TokenType type;
    private String value;
    private final int startIndex;

    public Token(TokenType type, String value, int startIndex) {
        this.type = type;
        this.value = value;
        this.startIndex = startIndex;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return String.format("[%s : %s] (%d)", type, value, startIndex);
    }

}
