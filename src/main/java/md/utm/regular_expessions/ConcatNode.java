package md.utm.regular_expessions;

import md.utm.utils.ColorManager;

import java.util.Arrays;
import java.util.List;

public class ConcatNode extends RegexNode {
    private final List<RegexNode> children;

    public ConcatNode(RegexNode... children) {
        this.children = Arrays.asList(children);
    }

    @Override
    public String generate(boolean reasoning, int level) {

        String indent = "• ".repeat(level);

        // Print reasoning messages only if reasoning is true
        if (reasoning) {
            System.out.print(indent + getColouredClassName() + ":: contains the following nodes: " +
                    children
                            .stream()
                            .map(c -> ColorManager.colorize(c.className(), ColorManager.WHITE_UNDERLINED))
                            .toList());
            System.out.println(". Iterating over them to generate a concatenated string...");
        }

        StringBuilder sb = new StringBuilder();
        for (RegexNode child : children) {
            if (reasoning) {
                System.out.println(indent + getColouredClassName() + ":: Processing " + ColorManager.colorize(child.className(), ColorManager.WHITE_UNDERLINED));
            }

            String generated = child.generate(reasoning, level + 1);

            if (reasoning) {
                System.out.println(indent + getColouredClassName() + ":: appended: " + ColorManager.colorize(generated, ColorManager.GREEN));
            }
            sb.append(generated);
        }

        // Final result message only if reasoning is true
        if (reasoning) {
            System.out.println(indent + getColouredClassNameFinal() + ":: Generated: " + ColorManager.colorize(sb.toString(), ColorManager.GREEN));
        }

        return sb.toString();
    }
}
