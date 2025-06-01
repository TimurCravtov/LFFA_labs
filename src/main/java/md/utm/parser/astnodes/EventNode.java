package md.utm.parser.astnodes;

import md.utm.parser.Position;

import static md.utm.utils.ColorManager.*;

public class EventNode extends StatementNode {
    private final String name;
    private final String time;
    private final String duration;
    private final String date;
    private final String dayOfWeek;
    private final String assignmentVariable;

    public EventNode(Position position, String name, String time, String duration, String date, String dayOfWeek, String assignmentVariable) {
        super(position);
        this.name = name;
        this.time = time;
        this.duration = duration;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.assignmentVariable = assignmentVariable;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDuration() {
        return duration;
    }

    public String getDate() {
        return date;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getAssignmentVariable() {
        return assignmentVariable;
    }

    public String toString() {
        return toString(" ");
    }

    public String toString(int indentationLevel) {
        StringBuilder buf = new StringBuilder();
        String indent = getLineIndent(indentationLevel + 1);
        buf.append(getLineIndent(indentationLevel)).append(getClass().getSimpleName());
        buf.append("\n");
        buf.append(String.format("%sSummary: %s\n%sTime: %s\n%sDate: %s", indent, colorize(name, CYAN), indent, colorize(time, CYAN), indent, colorize(date, CYAN)));
        buf.append('\n');

        return buf.toString();
    }

    @Override
    protected String toString(String indent) {
        return "";
    }

}