package md.utm.lab4.regex_nodes;

import md.utm.utils.ColorManager;

import java.util.Random;

public class ChoiceNode extends RegexNode {
    private final RegexNode left, right;

    public ChoiceNode(RegexNode left, RegexNode right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String generate(boolean reasoning, int level) {
        String indent = "• ".repeat(level);
        String generated;

        if (reasoning) {
            System.out.println(indent + getColouredClassName() +  ": choosing between left and right");
        }

        if (rand.nextBoolean()) {
            if (reasoning) {
                System.out.println(indent + getColouredClassName() +  ":: chosen: left " + String.format("(%s)", left.className()));
            }
            generated =  left.generate(reasoning, level + 1);
        } else {
            if (reasoning) {
                System.out.println(indent + getColouredClassName() + ":: chosen: right " + String.format("(%s)", right.className()));
            }
            generated =  right.generate(reasoning, level + 1);
        }

        System.out.println(indent + getColouredClassNameFinal() + ":: Generated: " + ColorManager.colorize(generated, ColorManager.GREEN));
        return generated;
    }
}
