package md.utm.parser.astnodes;

import md.utm.parser.Position;
import java.util.List;

import static md.utm.utils.ColorManager.*;

public class MergeNode extends StatementNode {
    private final List<String> sourceCalendars;
    private final String targetVariable;

    public MergeNode(Position position, List<String> sourceCalendars, String targetVariable) {
        super(position);
        this.sourceCalendars = sourceCalendars;
        this.targetVariable = targetVariable;
    }

    public List<String> getSourceCalendars() {
        return sourceCalendars;
    }

    public String getTargetVariable() {
        return targetVariable;
    }

    public String toString(int indentationLevel) {
        StringBuilder buf = new StringBuilder();
        String indent = getLineIndent(indentationLevel + 1);
        buf.append(getLineIndent(indentationLevel)).append(getClass().getSimpleName());
        buf.append("\n");
        buf.append(String.format("%sCalendarList: %s\n%sTarget: %s", indent, colorize(getSourceCalendars().toString(), CYAN), indent, colorize(targetVariable.toString(), CYAN)));
        buf.append('\n');

        return buf.toString();
    }

    @Override
    protected String toString(String indent) {
        return "";
    }
}