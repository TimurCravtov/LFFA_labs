package md.utm.parser.astnodes;

import md.utm.parser.Position;
import java.util.List;

public class IncludeNode extends StatementNode {
    private final List<String> entries;
    private final String calendarName;

    public IncludeNode(Position position, List<String> entries, String calendarName) {
        super(position);
        this.entries = entries;
        this.calendarName = calendarName;
    }

    public List<String> getEntries() {
        return entries;
    }

    public String getCalendarName() {
        return calendarName;
    }

    @Override
    protected String toString(String indent) {
        return "";
    }
}