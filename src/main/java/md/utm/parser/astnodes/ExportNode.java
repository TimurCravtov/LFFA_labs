package md.utm.parser.astnodes;

import md.utm.parser.Position;

import static md.utm.utils.ColorManager.*;

public class ExportNode extends StatementNode {
    private final String calendarVariable;
    private final String exportName;

    public ExportNode(Position position, String calendarVariable, String exportName) {
        super(position);
        this.calendarVariable = calendarVariable;
        this.exportName = exportName;
    }

    public String toString(int indentationLevel) {
        StringBuilder buf = new StringBuilder();
        String indent = getLineIndent(indentationLevel + 1);
        buf.append(getLineIndent(indentationLevel)).append(getClass().getSimpleName());
        buf.append("\n");
        buf.append(String.format("%sCalendar: %s\n%sExport path: %s", indent, colorize(calendarVariable, CYAN), indent, colorize(exportName, GREEN)));
        buf.append('\n');

        return buf.toString();
    }

    public String getCalendarVariable() {
        return calendarVariable;
    }

    public String getExportName() {
        return exportName;
    }

    @Override
    protected String toString(String indent) {
        return "";
    }
}