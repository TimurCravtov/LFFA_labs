package md.utm.lab4.regex_nodes;

import md.utm.utils.ColorManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConcatNode extends RegexNode {
    private final List<RegexNode> children;

    public ConcatNode(RegexNode... children) {
        this.children = Arrays.asList(children);
    }

    @Override
    public String generate(boolean reasoning, int level) {

        String indent = "• ".repeat(level);

        if (reasoning) {
            System.out.println(indent + getColouredClassName() +  ":: contains the following nodes: " + children.stream().map(c -> className()).toList());
            System.out.println(indent + "Iterating over them to generate a concatenated string...");
        }

        StringBuilder sb = new StringBuilder();
        for (RegexNode child : children) {
            System.out.println(indent + getColouredClassName() + ":: Processing " + ColorManager.colorize(child.className(), ColorManager.WHITE_UNDERLINED));

            String generated = child.generate(reasoning, level + 1);

            if (reasoning) {
                System.out.println(indent + getColouredClassName() + ":: appended: " + ColorManager.colorize(generated, ColorManager.GREEN));
            }
            sb.append(generated);
        }

        if (reasoning) {
            System.out.println(indent + "Generated: " + ColorManager.colorize(sb.toString(), ColorManager.GREEN));
        }

        return sb.toString();
    }
}
