package md.utm.lab4;

import md.utm.lab4.regex_nodes.*;
import md.utm.utils.ColorManager;


public class Main {
    public static void main(String[] args) {

        // (S|T)(U|V)w^*y+2u
        RegexNode root = new ConcatNode(
                new QuestionNode(
                        new ChoiceNode(
                                new LiteralNode("S"),
                                new LiteralNode("T")
                        )),

                new ChoiceNode(
                        new LiteralNode("U"),
                        new LiteralNode("V")
                ),
                new PowerNode(
                        new LiteralNode("w"),
                        "*"
                        ),
                new PowerNode(
                        new LiteralNode("y"),
                        "10"
                ),
                new LiteralNode("2"),
                new LiteralNode("u")
        );

        // Generate a random string from the tree

        String generated = root.generate(true, 1);

        System.out.println("\n\n -- Final generated string: -- ");
        System.out.println(ColorManager.colorize(generated, ColorManager.GREEN));


    }
}
