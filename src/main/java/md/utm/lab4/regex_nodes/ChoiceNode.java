package md.utm.lab4.regex_nodes;

import md.utm.utils.ColorManager;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static md.utm.utils.ColorManager.WHITE_UNDERLINED;
import static md.utm.utils.ColorManager.colorize;

public class ChoiceNode extends RegexNode {

    private final List<RegexNode> nodes;



    public ChoiceNode(RegexNode ... nodes) {
        this.nodes = Arrays.asList(nodes);
    }

    @Override
    public String generate(boolean reasoning, int level) {
        String indent = "• ".repeat(level);
        String generated;

        if (reasoning) {
            System.out.println(indent + getColouredClassName() + ": choosing between " + nodes.stream().map(c -> colorize(c.className(), WHITE_UNDERLINED)).toList().toString());
        }


        int chosenNodeIndex = rand.nextInt(nodes.size());

        if (reasoning) {
            System.out.println(indent + getColouredClassName() + ":: chosen: " + String.format("%d (%s)", chosenNodeIndex, nodes.get(chosenNodeIndex).className()));

        }

        generated = nodes.get(chosenNodeIndex).generate(reasoning, level + 1);


        if (reasoning) {
            System.out.println(indent + getColouredClassNameFinal() + ":: Generated: " + colorize(generated, ColorManager.GREEN));
        }

        return generated;
    }
}
