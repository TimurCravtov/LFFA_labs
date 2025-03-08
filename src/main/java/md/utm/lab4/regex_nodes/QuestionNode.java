package md.utm.lab4.regex_nodes;

import md.utm.utils.ColorManager;

public class QuestionNode extends RegexNode {
    private final RegexNode node;

    public QuestionNode(RegexNode node) {
        this.node = node;
    }

    @Override
    public String generate(boolean reasoning, int level) {

        boolean toGenerate = rand.nextBoolean();


        if (reasoning) {
            String indent = "• ".repeat(level);
            System.out.println(indent + getColouredClassName() + ":: Deciding whether to generate " + node.className());
            if (toGenerate) {
                System.out.println(indent + getColouredClassName() + ":: gave " + ColorManager.colorize("true", ColorManager.GREEN_BRIGHT) + ": Generating " + node.className());
                return node.generate(reasoning, level + 1);
            }
            else {
                System.out.println(indent + getColouredClassName() +  ":: gave " + ColorManager.colorize("false", ColorManager.RED) + " : Skipping " + node.className());
                return "";
            }
        }

        return null;
    }
}
