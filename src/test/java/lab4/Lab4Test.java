package lab4;

import md.utm.lab4.regex_nodes.*;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Lab4Test {

    @Test
    public void regex1() {
        RegexNode root = new ConcatNode(
                new ChoiceNode(
                        new LiteralNode("S"),
                        new LiteralNode("T")
                ),
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
                        "+"
                ),
                new LiteralNode("2"),

                new LiteralNode("u")
        );

        root.generate(true, 1);
        System.out.println(IntStream.range(0, 20).mapToObj(_ -> root.generate(false, 1)).collect(Collectors.toSet()));

    }

    @Test
    public void regex2() {
    }

    @Test
    public void regex3() {
        RegexNode root = new ConcatNode(
                new PowerNode(new LiteralNode("R"), "*"),
                new LiteralNode("S"),
                new ChoiceNode(
                        new LiteralNode("T"),
                        new LiteralNode("U"),
                        new LiteralNode("V")
                ),
                new LiteralNode("W"),
                new PowerNode(
                        new ChoiceNode(
                                new LiteralNode("x"),
                                new LiteralNode("y"),
                                new LiteralNode("z")
                        ), "2"
                )
        );

        root.generate(true, 1);
        System.out.println(IntStream.range(0, 20).mapToObj(_ -> root.generate(false, 1)).collect(Collectors.toSet()));
    }











    @Test
    public void regex4() {
        RegexNode root = new PowerNode(
                        new ChoiceNode(
                                new PowerNode(
                                        new LiteralNode("a"),
                                        "2"
                                ),
                                new LiteralNode("b")
                        ), "2");

        root.generate(true, 1);

        System.out.println(IntStream.range(0, 20).mapToObj(_ -> root.generate(false, 1)).collect(Collectors.toSet()));

    }

}
