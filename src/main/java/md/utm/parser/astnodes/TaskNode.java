package md.utm.parser.astnodes;

import md.utm.parser.Position;

public class TaskNode extends StatementNode {
    private final String name;
    private final String time;
    private final String repeatCondition;
    private final String assignmentVariable;

    public TaskNode(Position position, String name, String time, String repeatCondition, String assignmentVariable) {
        super(position);
        this.name = name;
        this.time = time;
        this.repeatCondition = repeatCondition;
        this.assignmentVariable = assignmentVariable;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getRepeatCondition() {
        return repeatCondition;
    }

    public String getAssignmentVariable() {
        return assignmentVariable;
    }

    @Override
    protected String toString(String indent) {
        return "";
    }
}