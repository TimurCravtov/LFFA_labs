package lab5;

import md.utm.grammar.DeriveRule;
import md.utm.grammar.Grammar;
import md.utm.grammar.Letter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Variant12GrammarToNormalize {
    public static Grammar get() {

        // Define non-terminal symbols (VN)
        Letter S = new Letter("S");
        Letter A = new Letter("A");
        Letter B = new Letter("B");
        Letter C = new Letter("C");
        Letter D = new Letter("D");
        Letter X = new Letter("X");

        // Define terminal symbols (VT)
        Letter a = new Letter("a");
        Letter b = new Letter("b");

        // Set of non-terminals (VN)
        Set<Letter> VN = new HashSet<>(Set.of(S, A, B, C, D, X));

        // Set of terminals (VT)
        Set<Letter> VT = new HashSet<>(Set.of(a, b));

        // Set of productions (P)
        Set<DeriveRule> productions = new HashSet<>();

        // Productions from the grammar
        // 1. S → A
        productions.add(new DeriveRule(S, List.of(A)));

        // 2. A → aX
        productions.add(new DeriveRule(A, List.of(a, X)));

        // 3. A → bX
        productions.add(new DeriveRule(A, List.of(b, X)));

        // 4. X → ε (epsilon)
        productions.add(new DeriveRule(X, Letter.EPSILON));

        // 5. X → BX
        productions.add(new DeriveRule(X, List.of(B, X)));

        // 6. X → b
        productions.add(new DeriveRule(X, List.of(b)));

        // 7. B → AD
        productions.add(new DeriveRule(B, List.of(A, D)));

        // 8. D → AD
        productions.add(new DeriveRule(D, List.of(A, D)));

        // 9. D → a
        productions.add(new DeriveRule(D, List.of(a)));

        // 10. C → Ca
        productions.add(new DeriveRule(C, List.of(C, a)));

        // Return the grammar G = (VN, VT, P, S)
        return new Grammar(VN, VT, productions, S);
    }
}