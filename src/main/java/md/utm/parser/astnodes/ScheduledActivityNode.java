package md.utm.parser.astnodes;

import md.utm.parser.Position;

public class ScheduledActivityNode extends StatementNode {
    public ScheduledActivityNode(Position position) {
        super(position);
    }

    @Override
    protected String toString(String indent) {
        return "";
    }
}
