package md.utm.lab4.regex_nodes;

import md.utm.utils.ColorManager;

public class LiteralNode extends RegexNode {
    private final String literal;

    public LiteralNode(String literal) {
        this.literal = literal;
    }

    @Override
    public String generate(boolean reasoning, int level) {

        if (reasoning) {
            System.out.println("• ".repeat(level) + getColouredClassNameFinal() +  ":: " + ColorManager.colorize(literal, ColorManager.GREEN)) ;
        }
        return literal;
    }
}




