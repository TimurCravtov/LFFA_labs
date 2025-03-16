package md.utm.chomsky_normal_form;

import md.utm.finite_automation.ChomskyType;
import md.utm.grammar.DeriveRule;
import md.utm.grammar.Grammar;
import md.utm.grammar.Letter;

import java.util.Set;
import java.util.stream.Collectors;

public class CNFService {
    public Grammar normalize(Grammar grammar) {

        if (grammar.getChomskyType(false) == ChomskyType.TYPE3) {
            System.out.println("The grammar is regular, returning the same instance of grammar");
            return grammar;
        }

        // creating copy of initial grammar units
        Set<Letter> initialV_T = Set.copyOf(grammar.getV_T());
        Set<Letter> initialV_N = Set.copyOf(grammar.getV_N());
        Set<DeriveRule> initialP = Set.copyOf(grammar.getP());
        Letter initialS = grammar.getS();


        // eliminating epsilon-transitions
        Set<DeriveRule> finalP = initialP.stream().filter(rule -> !isNullTransition(initialS,rule)).collect(Collectors.toSet());

        return null;
    }

    private boolean isNullTransition(Letter S, DeriveRule deriveRule) {
        return  deriveRule.getTo().getFirst().equals(Letter.EPSILON);
    }


    private boolean isUnitTransition(DeriveRule deriveRule, Set<Letter> V_N) {
        return false;
    }

}
