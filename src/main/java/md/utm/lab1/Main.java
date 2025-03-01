package md.utm.lab1;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        Grammar labOneGrammar = getLabOneGrammar();

        System.out.printf("-------The grammar-------%n%n%s%n", labOneGrammar);

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

    public static Grammar getLabOneGrammar() {
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
//                new DeriveRule(new Letter("B"), List.of(new Letter("b"), new Letter("c"))), // this line uncommented will produce NFA

                new DeriveRule(new Letter("D"), List.of(new Letter("a"), new Letter("D"))),
                new DeriveRule(new Letter("D"), List.of(new Letter("b"), new Letter("S"))),
//                new DeriveRule(new Letter("D"), List.of(new Letter("S"), new Letter("b"))), // this line uncommented will produce type-2 grammar

                new DeriveRule(new Letter("B"), List.of(new Letter("c"), new Letter("S"))),
//                new DeriveRule(List.of(new Letter("B"), new Letter("S"), new Letter("D")), new Letter("c")), // type 0
//                new DeriveRule(List.of(new Letter("B"), new Letter("S"), new Letter("D")), (List.of(new Letter("B"), new Letter("S"), new Letter("D"), new Letter("c")))), //type 1

                new DeriveRule(new Letter("D"), new Letter("c"))
        );

        return new Grammar(V_N, V_T, P, new Letter("S"));
    }
}
