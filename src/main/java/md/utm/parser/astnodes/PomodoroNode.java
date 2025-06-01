package md.utm.parser.astnodes;

import md.utm.parser.Position;

public class PomodoroNode extends StatementNode {
    private final String name;
    private final String time;
    private final int repetitions;
    private final String assignmentVariable;

    public PomodoroNode(Position position, String name, String time, int repetitions, String assignmentVariable) {
        super(position);
        this.name = name;
        this.time = time;
        this.repetitions = repetitions;
        this.assignmentVariable = assignmentVariable;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public String getAssignmentVariable() {
        return assignmentVariable;
    }

    @Override
    protected String toString(String indent) {
        return "";
    }
}