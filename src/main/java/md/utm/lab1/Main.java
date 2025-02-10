package md.utm.lab1;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        Set<Letter> V_N = Set.of(
                new Letter("S"),
                new Letter("B"),
                new Letter("D")
        );

        Set<Letter> V_T = Set.of(
                new Letter("a"),
                new Letter("b"),
                new Letter("c")
        );

        Set<DeriveRule> P = Set.of(
                new DeriveRule(new Letter("S"), List.of(new Letter("a"), new Letter("B"))),
                new DeriveRule(new Letter("B"), List.of(new Letter("a"), new Letter("D"))),
                new DeriveRule(new Letter("B"), List.of(new Letter("b"), new Letter("B"))),
                new DeriveRule(new Letter("D"), List.of(new Letter("a"), new Letter("D"))),
                new DeriveRule(new Letter("D"), List.of(new Letter("b"), new Letter("S"))),
                new DeriveRule(new Letter("B"), List.of(new Letter("c"), new Letter("S"))),
                new DeriveRule(new Letter("D"), new Letter("c"))
        );

        Grammar labOneGrammar = new Grammar(V_N, V_T, P, new Letter("S"));

        System.out.println(STR."-------The grammar-------\n\n\{labOneGrammar}");

        /* **************************************
            PART 2: Generating random words
        ************************************** */

        Set<String> words = new HashSet<>();
        System.out.println("\n-------Generating random strings-----\n");

        while (words.size() < 5) {
            words.add(labOneGrammar.generateRandomString(true));
        }
        System.out.println("\n-----5 generated words-------\n" + words);

        FiniteAutomaton finiteAutomaton = labOneGrammar.toFiniteAutomation();

        System.out.println(finiteAutomaton);
        System.out.println("\n----------Automaton generation------\n");

        String testString = "acc";
        String randomString = labOneGrammar.generateRandomString(false);

        System.out.println(STR."The verdict upon randomly generated string (should be always true) \{randomString} is : \{finiteAutomaton.belongsToAutomation(LetterListHelper.getLetterListFromString(randomString))}");

        System.out.println(STR."The verdict upon string \{testString} is : \{finiteAutomaton.belongsToAutomation(LetterListHelper.getLetterListFromString(testString))}");
    }
}
