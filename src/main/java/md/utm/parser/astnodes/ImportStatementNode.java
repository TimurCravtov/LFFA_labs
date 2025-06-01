package md.utm.parser.astnodes;

import md.utm.parser.Position;

import static md.utm.utils.ColorManager.*;

public class ImportStatementNode extends StatementNode {
    private final String path;
    private final String alias;

    public ImportStatementNode(Position position, String path, String alias) {
        super(position);
        this.path = path;
        this.alias = alias;
    }

    public String getPath() {
        return path;
    }

    public String getAlias() {
        return alias;
    }

    public String toString(int indentationLevel) {
        StringBuilder buf = new StringBuilder();
        String indent = getLineIndent(indentationLevel + 1);
        buf.append(getLineIndent(indentationLevel)).append(getClass().getSimpleName());
        buf.append("\n");
        buf.append(String.format("%sPath: %s\n%sAlias: %s", indent, colorize(path, GREEN), indent, colorize(alias, CYAN)));
        buf.append('\n');

        return buf.toString();
    }

    @Override
    protected String toString(String indent) {
        return "";
    }
}
