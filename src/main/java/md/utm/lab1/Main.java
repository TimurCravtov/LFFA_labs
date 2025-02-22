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

        System.out.println(String.format("-------The grammar-------%n%n%s", labOneGrammar));

        /* **************************************
            PART 2: Generating random words
        ************************************** */

        Set<String> words = new HashSet<>();
        System.out.println("\n-------Generating random strings-----\n");

        while (words.size() < 5) {
            words.add(labOneGrammar.generateRandomString(true));
        }
        System.out.printf("\n-----5 generated words-------\n%s%n", words);

        FiniteAutomaton finiteAutomaton = labOneGrammar.toFiniteAutomation();

        System.out.println(finiteAutomaton);
        System.out.println("\n----------Automaton generation------\n");

        String testString = "abcabcabcabcaababaac";
        String randomString = labOneGrammar.generateRandomString(false);

        System.out.printf(
                "The verdict upon randomly generated string (should be always true) %s is : %s%n",
                randomString, finiteAutomaton.belongsToAutomation(LetterListHelper.getLetterListFromString(randomString))
        );

        System.out.printf(
                "The verdict upon string %s is : %s%n",
                testString, finiteAutomaton.belongsToAutomation(LetterListHelper.getLetterListFromString(testString))
        );
    }
}
