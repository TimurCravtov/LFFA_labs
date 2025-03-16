package md.utm.regular_expessions;

import md.utm.utils.ColorManager;

public class QuestionNode extends RegexNode {
    private final RegexNode node;

    public QuestionNode(RegexNode node) {
        this.node = node;
    }

    @Override
    public String generate(boolean reasoning, int level) {
        boolean toGenerate = rand.nextBoolean();
        String indent = "• ".repeat(level);

        // Print reasoning messages only if reasoning is true
        if (reasoning) {
            System.out.println(indent + getColouredClassName() + ":: Deciding whether to generate " + node.className());
            if (toGenerate) {
                System.out.println(indent + getColouredClassName() + ":: gave " + ColorManager.colorize("true", ColorManager.GREEN_BRIGHT) + ": Generating " + node.className());
            } else {
                System.out.println(indent + getColouredClassName() + ":: gave " + ColorManager.colorize("false", ColorManager.RED) + ": Skipping " + node.className());
            }
        }

        if (toGenerate) {
            var generated = node.generate(reasoning, level + 1);
            if (reasoning) {
                System.out.println(indent + getColouredClassNameFinal() + ":: Generated " + ColorManager.colorize(generated, ColorManager.GREEN));
            }
            return generated;

        } else {
            return "";
        }
    }
}
