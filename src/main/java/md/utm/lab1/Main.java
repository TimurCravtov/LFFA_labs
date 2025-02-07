package md.utm.lab1;

import java.util.*;

public class Main {


    public static void main(String[] args) {

        /* *************************************
            PART 1: Generating the grammar,
            using alphabet, productions, etc
        ************************************* */

        LocalLetterFactory llf = new LocalLetterFactory();

        Set<Letter> V_N = Set.of(
                llf.create("S", false),
                llf.create("B", false),
                llf.create("D", false)
        );

        Set<Letter> V_T = Set.of(
                llf.create("a", true),
                llf.create("b", true),
                llf.create("c", true)
        );

        Set<DeriveRule> P = Set.of(
                new DeriveRule(llf.get("S"), List.of(llf.get("a"), llf.get("B"))),
                new DeriveRule(llf.get("B"), List.of(llf.get("a"), llf.get("D"))),
                new DeriveRule(llf.get("B"), List.of(llf.get("b"), llf.get("B"))),
                new DeriveRule(llf.get("D"), List.of(llf.get("a"), llf.get("D"))),
                new DeriveRule(llf.get("D"), List.of(llf.get("b"), llf.get("S"))),
                new DeriveRule(llf.get("B"), List.of(llf.get("c"), llf.get("S"))),
                new DeriveRule(llf.get("D"), llf.get("c"))
        );

        Grammar labOneGrammar = new Grammar(V_N, V_T, P, llf.get("S"));

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

        System.out.println("\n----------automation generation------\n");




        String testString = "acaaaaababaaaaaaaaaaaaaabababbac";
        String randomString = labOneGrammar.generateRandomString(false);

        System.out.println(STR."The verdict upon rangom generated string (should be always true) \{randomString} is : \{finiteAutomaton.belongsToAutomation(llf.getLetterListFromString(randomString))}");

        System.out.println(STR."The verdict upon string \{testString} is : \{finiteAutomaton.belongsToAutomation(llf.getLetterListFromString(testString))}");
    }
}
