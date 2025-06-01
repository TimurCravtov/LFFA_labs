package lab5;

import md.utm.chomsky_normal_form.CNFService;
import md.utm.grammar.DeriveRule;
import md.utm.grammar.Grammar;
import md.utm.grammar.Letter;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static md.utm.utils.ColorManager.*;

public class NormalizationTest {


    @Test
    public void testNormalization() {
        Grammar grammar = VariantGrammarToNormalize.get12();
        CNFService cnfService = new CNFService(grammar);
        cnfService.normalize();
        System.out.println(cnfService);
    }

    @Test
    public void textWithReasoning() {
        Grammar grammar = VariantGrammarToNormalize.get15();
//        Grammar grammar = getGrammar();

        System.out.println(colorize("Initial grammar:", CYAN));
        System.out.println(grammar);

        CNFService cnfService = new CNFService(grammar);

        System.out.println(colorize("Grammar after resolving S rule: ", YELLOW));
        cnfService.resolveStartingSymbol();
        System.out.println(cnfService.getGrammar());

        System.out.println(colorize("Grammar after removing inaccessible", BLUE));
        cnfService.removeInaccessible();
        System.out.println(cnfService.getGrammar());

        System.out.println(colorize("Grammar after eliminating epsilon transitions: ", RED));
        cnfService.eliminateEpsilonTransitions();
        System.out.println(cnfService.getGrammar());

        System.out.println(colorize("Grammar after eliminating renamings: ", BLUE));
        cnfService.eliminateRenamings();
        System.out.println(cnfService.getGrammar());

        System.out.println(colorize("Grammar after shorty-fying ", BLUE));
        cnfService.replaceLongProductions();
        System.out.println(cnfService.getGrammar());

        System.out.println(colorize("Grammar after replacing terminals with non-terminals", BLUE));
        cnfService.replaceTerminalsWithIntermediate();
        System.out.println(cnfService.getGrammar());

        System.out.println(colorize("Grammar after removing repetitions", BLUE));
        cnfService.removeRepetitions();
        cnfService.removeRepetitions();
        System.out.println(cnfService.getGrammar());


    }

    public Grammar getGrammar() {
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
        productions.add(new DeriveRule(A, List.of(A, B, a, b)));
        productions.add(new DeriveRule(B, a));
        productions.add(new DeriveRule(B, A));

        productions.add(new DeriveRule(B, List.of(b, S)));


        return new Grammar(VN, VT, productions, S);
    }

}
