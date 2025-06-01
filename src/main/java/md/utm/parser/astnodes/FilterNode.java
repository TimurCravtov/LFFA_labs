package md.utm.parser.astnodes;

import md.utm.parser.Position;

public class FilterNode extends StatementNode {
    private final String sourceCalendar;
    private final String field;
    private final String operator;
    private final String value;
    private final String targetVariable;

    public FilterNode(Position position, String sourceCalendar, String field, String operator, String value, String targetVariable) {
        super(position);
        this.sourceCalendar = sourceCalendar;
        this.field = field;
        this.operator = operator;
        this.value = value;
        this.targetVariable = targetVariable;
    }

    public String getSourceCalendar() {
        return sourceCalendar;
    }

    public String getField() {
        return field;
    }

    public String getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }

    public String getTargetVariable() {
        return targetVariable;
    }

    @Override
    protected String toString(String indent) {
        return "";
    }
}