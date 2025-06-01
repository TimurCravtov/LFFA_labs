package md.utm.parser.astnodes;

import md.utm.parser.Position;

public abstract class ASTNode {
    private final Position position;

    protected ASTNode(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "";
    }

    protected String toString(int indentationLevel) {
        return "";
    }

    protected final String getLineIndent(int level) {
        char g = '⌝';
        return g + "--".repeat(level) + " ";
    }

    protected abstract String toString(String indent);

}


