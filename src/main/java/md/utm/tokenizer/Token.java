package md.utm.tokenizer;

import md.utm.parser.Position;

public class Token {
    private TokenType type;
    private String value;
    private final int startIndex;

    private final Position position;


    public Token(TokenType type, String value, int startIndex) {
        this.type = type;
        this.value = value;
        this.startIndex = startIndex;
        this.position = new Position(0, startIndex); // fix this someday
    }

    public int getStartIndex() {
        return startIndex;
    }

    public Position getPosition() {
        return position;
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
