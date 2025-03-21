package lab5;

import md.utm.grammar.DeriveRule;
import md.utm.grammar.Grammar;
import md.utm.grammar.Letter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Variant12GrammarToNormalize {
    public static Grammar get() {

        Letter S = new Letter("S");
        Letter D = new Letter("D");
        Letter B = new Letter("B");
        Letter A = new Letter("A");
        Letter C = new Letter("C");

        Letter a  = new Letter("a");
        Letter b = new Letter("b");


        Set<Letter> VN = new HashSet<>(Set.of(A,B,C,D,S));
        Set<Letter>  VT = new HashSet<>(Set.of(a,b));

        Set<DeriveRule> productions = new HashSet<>();


        productions.add(new DeriveRule(S, List.of(a, B)));
        productions.add(new DeriveRule(S, List.of(b, A)));
        productions.add(new DeriveRule(S, B));
        productions.add(new DeriveRule(S, List.of(a, A)));
        productions.add(new DeriveRule(A, Letter.EPSILON));
        productions.add(new DeriveRule(A, List.of(a, S)));
        productions.add(new DeriveRule(A, List.of(A, B, a, b)));
        productions.add(new DeriveRule(B, a));


//        productions.add(new DeriveRule(B, Letter.EPSILON)); //
//        productions.add(new DeriveRule(C, Letter.EPSILON)); //

        productions.add(new DeriveRule(B, List.of(b, S)));

        productions.add(new DeriveRule(C, List.of(a, b, C)));
        productions.add(new DeriveRule(D, List.of(A, B)));

        return new Grammar(VN, VT, productions, S);

    }
}
