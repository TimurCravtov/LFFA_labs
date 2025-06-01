package md.utm.parser.astnodes;

import md.utm.parser.Position;

public abstract class StatementNode extends ASTNode {
    public StatementNode(Position position) {
        super(position);
    }
}
