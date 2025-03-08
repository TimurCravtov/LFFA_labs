package md.utm.lab4;

import md.utm.lab4.regex_nodes.*;
import md.utm.utils.ColorManager;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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

        String generated = root.generate(true, 0);

        System.out.println("\n -- Final generated string: -- ");
        System.out.println(ColorManager.colorize(generated, ColorManager.GREEN));

        System.out.println(IntStream.range(0, 20)
                .mapToObj(e -> root.generate(false, 0))
                .collect(Collectors.toCollection(HashSet::new)).toString());
    }
}
