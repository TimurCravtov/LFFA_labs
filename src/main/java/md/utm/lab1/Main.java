package md.utm.lab1;

import java.util.*;

public class Main {
    public static void main(String[] args) {



         /* *************************************
            PART 1: Generating the grammar,
            using alphabet, productions, etc
          ************************************* */


        // Predefine the letters
        Letter S = new Letter("S", false);
        Letter B = new Letter("B", false);
        Letter D = new Letter("D", false);
        Letter a = new Letter("a", true);
        Letter b = new Letter("b", true);
        Letter c = new Letter("c", true);

        Set<Letter> V_N = new HashSet<>(Set.of(S, B, D));

        Set<Letter> V_T = new HashSet<>(Set.of(a, b, c));

        Set<DeriveRule> P = new HashSet<>(Set.of(
                new DeriveRule(S, List.of(a, B)),
                new DeriveRule(B, List.of(a, D)),
                new DeriveRule(B, List.of(b, B)),
                new DeriveRule(D, List.of(a, D)),
                new DeriveRule(D, List.of(b, S)),
                new DeriveRule(B, List.of(c, S)),
                new DeriveRule(D, c)
        ));

        Grammar labOneGrammar = new Grammar(V_N, V_T, P, S);

        Set<String> words = new HashSet<>();

        while (words.size() < 5) {
            words.add(labOneGrammar.generateRandomString(true));
        }

        System.out.println(words);

          /* **************************************
            PART 2: Generating the finite automation
            based on grammar
          **************************************** */

        FiniteAutomaton finiteAutomaton = labOneGrammar.toFiniteAutomation();

    }
}
