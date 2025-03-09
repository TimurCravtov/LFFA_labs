package md.utm.lab4.regex_nodes;

import md.utm.utils.ColorManager;

public class PowerNode extends RegexNode {
    private final RegexNode node;
    private final String power;

    public PowerNode(RegexNode node, String power) {
        this.node = node;
        this.power = power;
    }

    @Override
    public String generate(boolean reasoning, int level) {
        StringBuilder result = new StringBuilder();
        String indent = "• ".repeat(level);

        if (reasoning) {

            System.out.println(indent + getColouredClassName() + ":: Applying '" + ColorManager.colorize(power, ColorManager.PURPLE) + "' to " + node.className());
        }

        int repeatCount;
        if (power.equals("*")) {
            repeatCount = rand.nextInt(6); // 0 to 5 times
        } else if (power.equals("+")) {
            repeatCount = rand.nextInt(5) + 1; // 1 to 5 times
        } else {
            repeatCount = Integer.parseInt(power);
        }

        if (reasoning) {
            System.out.println(indent + getColouredClassName() + ":: Repeating " + node.className() + " " + repeatCount + " times");
        }

        for (int i = 0; i < repeatCount; i++) {
            if (reasoning) {
                System.out.println(indent + getColouredClassName() + ":: Iteration " + (i + 1));
            }
            String generated = node.generate(reasoning, level + 1);
            result.append(generated);

            if (reasoning) {
                System.out.println(indent + getColouredClassName() + ":: Appended " + ColorManager.colorize(generated, ColorManager.GREEN));
            }
        }

        if (reasoning) {
            System.out.println(indent + getColouredClassNameFinal() + ":: Generated " + ColorManager.colorize(result.toString(), ColorManager.GREEN));
        }

        return result.toString();
    }
}
