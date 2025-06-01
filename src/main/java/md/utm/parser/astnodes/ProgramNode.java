package md.utm.parser.astnodes;

import md.utm.parser.Position;
import java.util.List;

public class ProgramNode extends ASTNode {
    private final List<StatementNode> statements;

    public ProgramNode(Position position, List<StatementNode> statements) {
        super(position);
        this.statements = statements;
    }

    public List<StatementNode> getStatements() {
        return statements;
    }

    public String toString(int indentationLevel) {
        StringBuilder buf = new StringBuilder();
        buf.append(getLineIndent(indentationLevel)).append(getClass().getSimpleName());
        buf.append('\n');
        for (StatementNode statement : statements) {
            buf.append(statement.toString(indentationLevel + 1));
        }
        return buf.toString();
    }


    public String toString() {
        return toString(0);
    }

    @Override
    public String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("ProgramNode\n");
        for (StatementNode statement : statements) {
            sb.append(statement.toString(indent + "  ")).append("\n");
        }
        return sb.toString().trim();
    }

}