package md.utm.chomsky_normal_form;

import md.utm.finite_automation.ChomskyType;
import md.utm.grammar.DeriveRule;
import md.utm.grammar.Grammar;
import md.utm.grammar.Letter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CNFService {

    private final Grammar initialGrammar;
    public CNFService(Grammar grammar) {
        initialGrammar = grammar;
    }


    public Grammar normalize() {


        VariableFactory variableFactory = new VariableFactory(initialGrammar.getV_T(), initialGrammar.getV_N());

        // creating copy of initial grammar units
        Set<Letter> initialV_T =  Set.copyOf(initialGrammar.getV_T());

        Set<Letter> finalV_T = Set.copyOf(initialGrammar.getV_T());
        Set<Letter> finalV_N = Set.copyOf(initialGrammar.getV_N());
        Set<DeriveRule> finalP = new java.util.HashSet<>(Set.copyOf(initialGrammar.getP()));
        finalP.removeIf(this::isNullTransition);

        // if S is on RHS, adds a transition S0 -> S
        if (hasSOnRight(initialGrammar.getS(), initialGrammar.getP())) {

            Letter newStart = variableFactory.getNewStartLetter();
            finalP.add(new DeriveRule(newStart, initialGrammar.getS()));
        }

        return null;
    }


    public Set<Letter> extractNullables() {
        Set<Letter> VNcopy = Set.copyOf(initialGrammar.getV_N());

        return VNcopy.stream()
                .filter(letter -> isNullable(
                        letter,
                        initialGrammar.getP(),
                        initialGrammar.getV_T(),
                        initialGrammar.getV_N(),
                        new HashSet<>()) // New visited set for each letter
                ).collect(Collectors.toSet());
    }

    public boolean isNullable(Letter forCheck,
                              Collection<DeriveRule> rules,
                              Set<Letter> VT,
                              Set<Letter> VN,
                              Set<Letter> visited) {

        // If already visited, return false to avoid infinite recursion
        if (visited.contains(forCheck)) {
            return false;
        }

        // Mark this node as visited
        visited.add(forCheck);

        Collection<DeriveRule> thisLetterRules = rules.stream()
                .filter(r -> r.getFrom().getFirst().equals(forCheck))
                .toList();

        // Directly nullable (A -> ε)
        if (hasNullTransition(thisLetterRules)) {
            return true;
        }

        // Recursive case: check if all symbols in the right-hand side are nullable
        for (DeriveRule rule : thisLetterRules) {
            // All letters on the right-hand side must be nullable
            boolean allNullable = rule.getTo().stream()
                    .allMatch(r1 -> isNullable(r1, rules, VT, VN, new HashSet<>(visited)));

            if (allNullable) {
                return true;
            }
        }

        return false;
    }


    public boolean hasNullTransition(Collection<DeriveRule> rules) {
//        rules.forEach(r -> System.out.println("Checking rule: " + r + ", isNull? " + isNullTransition(r)));
        return rules.stream().anyMatch(this::isNullTransition);
    }

    public boolean isNullTransition(DeriveRule deriveRule) {
        List<Letter> rhs = deriveRule.getTo();

        return rhs.size() == 1 && rhs.getFirst().equals(Letter.EPSILON);
    }


    public boolean hasSOnRight(Letter S, Collection<DeriveRule> rules) {
        return rules.stream().anyMatch(deriveRule -> deriveRule.getTo().contains(S));
    }

    public boolean isUnitTransition(DeriveRule deriveRule, Set<Letter> V_N) {
        return false;
    }



}
