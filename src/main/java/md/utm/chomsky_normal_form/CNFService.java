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

    public Set<DeriveRule> eliminateEpsilonTransitions() {
        Set<Letter> nullables = extractNullables();

        // Start with a copy of the original rules, minus ε rules (clean base)
        Set<DeriveRule> newRules = new HashSet<>(initialGrammar.getP());
        newRules.removeIf(this::isNullTransition);

        Set<DeriveRule> generatedRules = new HashSet<>();

        // Go through all existing rules
        for (DeriveRule rule : initialGrammar.getP()) {
            List<Letter> rhs = rule.getTo();


            List<Integer> nullableIndexes = new java.util.ArrayList<>();
            for (int i = 0; i < rhs.size(); i++) {
                if (nullables.contains(rhs.get(i))) {
                    nullableIndexes.add(i);
                }
            }

            // Generate combinations of nullable indexes to remove
            Set<List<Letter>> combinations = generateNullableCombinations(rhs, nullableIndexes);

            // For each combination, create a new rule if it's different from the original RHS
            for (List<Letter> combination : combinations) {
                if (combination.isEmpty()) {
                    continue; // We don't add empty rules here (A -> ε) unless explicitly required
                }

                DeriveRule newRule = new DeriveRule(rule.getFrom(), combination);
                generatedRules.add(newRule);
            }
        }

        // Combine the rules: original (without ε) + generated
        newRules.addAll(generatedRules);

        return newRules;
    }

    private Set<List<Letter>> generateNullableCombinations(List<Letter> rhs, List<Integer> nullableIndexes) {
        Set<List<Letter>> combinations = new HashSet<>();

        int totalNullable = nullableIndexes.size();

        // 2^nullableIndexes.size() combinations
        int combinationsCount = 1 << totalNullable;

        // in mask each bit 10001 means 1 - keep, 0 - discard
        for (int mask = 1; mask < combinationsCount; mask++) {

            List<Letter> newCombination = new java.util.ArrayList<>();

            // For each letter in rhs, include it unless it's nullable, and we decide to exclude it
            for (int i = 0; i < rhs.size(); i++) {
                if (!nullableIndexes.contains(i)) {

                    newCombination.add(rhs.get(i));
                } else {

                    // we have AbcdEfG. (for simplicity, each terminal is nullable. So nullable indexes = [0, 4, 6] )
                    // if we're currently working on letter E, it's index in rhs is 4. The 4th index in nullableIndexes is 1st index.
                    // now returning to mask.
                    // if we have a mask 011, we discard E and G.
                    // So, if we apply to mask the 1st index we get:

                    // 1 << 1 = 01
                    // mask & 01 = 011 & 001 = 1. If 1, we discard, otherwise, keep.
                    // special thanks to Olga Grosu and Viorel Bostan for teaching us boolean algrebra
                    // will anyone ever read this? I don't think so.
                    int nullableIndex = nullableIndexes.indexOf(i);
                    boolean keep = (mask & (1 << nullableIndex)) == 0;
                    if (keep) {
                        newCombination.add(rhs.get(i));
                    }
                }
            }
            combinations.add(newCombination);
        }

        return combinations;
    }


    private boolean isUnit(DeriveRule rule) {
        List<Letter> lhs = rule.getFrom();
        List<Letter> rhs = rule.getTo();

        // returns true if sizes of left and right part are 1 and both are non-terminal
        return lhs.size() == 1 && rhs.size() == 1 && this.initialGrammar.getV_N().containsAll(Set.of(lhs.getFirst(), rhs.getFirst()));

    }

    public void eliminateRenamings() {

    }

    public void eliminateInaccessible() {

    }

    public void eliminateNonProductiveSymbols() {

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
