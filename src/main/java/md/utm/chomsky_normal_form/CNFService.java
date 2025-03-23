package md.utm.chomsky_normal_form;

import md.utm.grammar.DeriveRule;
import md.utm.grammar.Grammar;
import md.utm.grammar.Letter;

import java.util.*;
import java.util.stream.Collectors;

public class CNFService {

    private final Grammar grammar;

    private final VariableFactory variableFactory;


    public CNFService(Grammar initialGrammar) {

        // creating copies of the data, so we don't change anything in new grammar
        Set<Letter> V_N_copy = new HashSet<>(initialGrammar.getV_N());
        Set<Letter> V_T_copy = new HashSet<>(initialGrammar.getV_T());
        Set<DeriveRule> P_copy = new HashSet<>(initialGrammar.getP());
        Letter S_copy = initialGrammar.getS();

        this.grammar = new Grammar(V_N_copy, V_T_copy, P_copy, S_copy);

        this.variableFactory = new VariableFactory(V_T_copy, V_N_copy);

    }


    public Grammar getGrammar() {
        return grammar;
    }

    public String toString() {
        return grammar.toString();
    }

    public Grammar normalize() {

        resolveStartingSymbol();
        eliminateEpsilonTransitions();
        eliminateRenamings();

        return this.grammar;

    }

    /**
     * If S is met in right side of production (E.g. A -> balbeS), then we create a new rule (S0 -> S), and set S0 as new S.
     */
    public void resolveStartingSymbol() {
        if (hasSOnRight(grammar.getS(), grammar.getP())) {
            Letter newStart = variableFactory.getNewStartLetter();
            this.grammar.getP().add(new DeriveRule(newStart, grammar.getS()));
            this.grammar.setS(newStart);
            this.grammar.getV_N().add(newStart);
        }
    }

    public void eliminateEpsilonTransitions() {
        Set<Letter> nullables = extractNullables();

        // Start with a copy of the original rules, minus ε rules (clean base)
        Set<DeriveRule> newRules = new HashSet<>(grammar.getP());
        newRules.removeIf(this::isNullTransition);

        Set<DeriveRule> generatedRules = new HashSet<>();

        // Go through all existing rules
        for (DeriveRule rule : grammar.getP()) {
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
        this.grammar.setP(newRules);
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


    public void replaceLongProductions() {

        HashSet<DeriveRule> newProductions = new HashSet<>();

        for (DeriveRule rule : grammar.getP()) {
            HashSet<DeriveRule> eachStepNewRules = new HashSet<>();
            shortify(rule, eachStepNewRules);
            newProductions.addAll(eachStepNewRules);
        }

        grammar.getP().removeIf(rule -> rule.getTo().size() > 2);
        grammar.getP().addAll(newProductions);
    }

    public void shortify(DeriveRule rule, Set<DeriveRule> returningSet) {
        if (rule.getTo().size() > 2) {

            Letter newLetter = variableFactory.getNextNonterminal();
            grammar.getV_N().add(newLetter);
            List<Letter> newRhs = rule.getTo().subList(1, rule.getTo().size());

            DeriveRule ruleToAdd = new DeriveRule(rule.getFrom().getFirst(), List.of(rule.getTo().getFirst(), newLetter));
            returningSet.add(ruleToAdd);

            DeriveRule shorterRule = new DeriveRule(newLetter, newRhs);
            shortify(shorterRule, returningSet);

        }
        else {
            returningSet.add(rule);
        }
    }

    public void replaceTerminalsWithIntermediate() {
        final int MAX_NON_TERMINAL_COUNT = 2;
        Set<DeriveRule> newRules = new HashSet<>();

        // Iterate through each rule in the grammar's production set
        for (DeriveRule rule : grammar.getP()) {
            List<Letter> rhs = rule.getTo();

            // Skip rules with RHS length < 2
            if (rhs.size() < 2) {
                newRules.add(rule);  // Preserve the original rule
                continue;
            }

            List<Letter> updatedRhs = new ArrayList<>(rhs);  // Create a mutable copy
            int replacementsMade = 0;

            // Replace up to MAX_NON_TERMINAL_COUNT terminals
            for (int i = 0; i < rhs.size() && replacementsMade < MAX_NON_TERMINAL_COUNT; i++) {
                Letter letter = rhs.get(i);
                if (grammar.getV_T().contains(letter)) {  // Check if it's a terminal
                    Letter newNonTerminal = replaceWithNonTerminal(letter);
                    updatedRhs.set(i, newNonTerminal);
                    newRules.add(new DeriveRule(newNonTerminal, List.of(letter)));
                    replacementsMade++;
                }
            }

            // Add the updated rule
            newRules.add(new DeriveRule(rule.getFrom(), updatedRhs));
        }

        // Update the grammar's production rules
        grammar.getP().clear();
        grammar.getP().addAll(newRules);
    }




    private Letter replaceWithNonTerminal(Letter letter) {
        return variableFactory.getNonterminalNewLetter(letter);
    }


    private boolean isUnit(DeriveRule rule) {
        List<Letter> lhs = rule.getFrom();
        List<Letter> rhs = rule.getTo();

        // returns true if sizes of left and right part are 1 and both are non-terminal
        return lhs.size() == 1 && rhs.size() == 1 && this.grammar.getV_N().containsAll(Set.of(lhs.getFirst(), rhs.getFirst()));

    }

    public void eliminateRenamings() {
        // Start with a copy of the current production rules
        Set<DeriveRule> newRules = new HashSet<>(grammar.getP());

        // Step 1: Identify all unit productions and build a renaming map
        Set<DeriveRule> unitRules = newRules.stream()
                .filter(this::isUnit)
                .collect(Collectors.toSet());

        // Step 2: Compute the transitive closure of renaming for each non-terminal
        Map<Letter, Set<Letter>> renamingClosure = new HashMap<>();
        for (Letter nonTerminal : grammar.getV_N()) {
            renamingClosure.put(nonTerminal, getRenamingClosure(nonTerminal, unitRules, new HashSet<>()));
        }

        // Step 3: For each non-terminal, add non-unit productions from all reachable non-terminals
        Set<DeriveRule> generatedRules = new HashSet<>();
        for (Letter from : grammar.getV_N()) {
            Set<Letter> reachable = renamingClosure.get(from);
            for (Letter to : reachable) {
                // Find all non-unit productions from 'to'
                Set<DeriveRule> nonUnitProductions = grammar.getP().stream()
                        .filter(rule -> rule.getFrom().getFirst().equals(to) && !isUnit(rule))
                        .collect(Collectors.toSet());
                // Rewrite them to start from 'from'
                for (DeriveRule rule : nonUnitProductions) {
                    generatedRules.add(new DeriveRule(from, rule.getTo()));
                }
            }
        }

        // Step 4: Update the grammar's production rules
        newRules.removeIf(this::isUnit); // Remove all unit productions
        newRules.addAll(generatedRules); // Add the new non-unit productions

        // Replace the old rules with the new ones
        grammar.getP().clear();
        grammar.getP().addAll(newRules);
    }

    /**
     * Recursively compute the set of non-terminals reachable via renaming rules from a given non-terminal.
     * @param start The starting non-terminal
     * @param unitRules The set of unit production rules
     * @param visited Set to track visited non-terminals to avoid infinite recursion in cycles
     * @return Set of all non-terminals reachable from 'start' via unit productions
     */
    private Set<Letter> getRenamingClosure(Letter start, Set<DeriveRule> unitRules, Set<Letter> visited) {
        Set<Letter> closure = new HashSet<>();
        closure.add(start); // Include the starting non-terminal

        if (visited.contains(start)) {
            return closure; // Avoid infinite loops in cycles like A -> B -> C -> A
        }
        visited.add(start);

        // Find all unit rules of the form 'start -> X'
        Set<Letter> directRenamings = unitRules.stream()
                .filter(rule -> rule.getFrom().getFirst().equals(start))
                .map(rule -> rule.getTo().getFirst())
                .collect(Collectors.toSet());

        // Recursively explore each directly reachable non-terminal
        for (Letter next : directRenamings) {
            closure.addAll(getRenamingClosure(next, unitRules, new HashSet<>(visited)));
        }

        return closure;
    }

    public void eliminateInaccessible() {

    }

    public void eliminateNonProductiveSymbols() {

    }



    public Set<Letter> extractNullables() {
        Set<Letter> VNcopy = Set.copyOf(grammar.getV_N());

        return VNcopy.stream()
                .filter(letter -> isNullable(
                        letter,
                        grammar.getP(),
                        grammar.getV_T(),
                        grammar.getV_N(),
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
        return rhs.size() == 1 && rhs.getFirst().equals(Letter.EPSILON) && !deriveRule.getTo().equals(grammar.getS());
    }


    public boolean hasSOnRight(Letter S, Collection<DeriveRule> rules) {
        return rules.stream().anyMatch(deriveRule -> deriveRule.getTo().contains(S));
    }

    public boolean isUnitTransition(DeriveRule deriveRule, Set<Letter> V_N) {
        return false;
    }

    public void removeInaccessible() {
        // Step 1: Find all accessible symbols starting from S
        Set<Letter> accessible = new HashSet<>();
        Set<Letter> visited = new HashSet<>();

        // Start with the start symbol
        accessible.add(grammar.getS());
        findAccessibleSymbols(grammar.getS(), accessible, visited);

        // Step 2: Remove rules containing inaccessible symbols
        Set<DeriveRule> newRules = grammar.getP().stream()
                .filter(rule -> accessible.contains(rule.getFrom().getFirst()) &&
                        rule.getTo().stream().allMatch(letter ->
                                letter.equals(Letter.EPSILON) ||
                                        accessible.contains(letter)))
                .collect(Collectors.toSet());

        // Step 3: Update non-terminals (V_N) and rules (P)
        Set<Letter> newVN = grammar.getV_N().stream()
                .filter(accessible::contains)
                .collect(Collectors.toSet());

        grammar.setP(newRules);
        grammar.setV_N(newVN);
    }

    /**
     * Recursively finds all symbols accessible from the given letter.
     * @param current The current symbol being processed
     * @param accessible Set to store all accessible symbols
     * @param visited Set to prevent infinite recursion
     */
    private void findAccessibleSymbols(Letter current, Set<Letter> accessible, Set<Letter> visited) {
        if (visited.contains(current)) {
            return; // Avoid cycles
        }

        visited.add(current);

        // Find all rules where 'current' is on the left-hand side
        Set<DeriveRule> relevantRules = grammar.getP().stream()
                .filter(rule -> rule.getFrom().getFirst().equals(current))
                .collect(Collectors.toSet());

        // For each rule, add right-hand side symbols to accessible set
        for (DeriveRule rule : relevantRules) {
            for (Letter letter : rule.getTo()) {
                if (!letter.equals(Letter.EPSILON) && !accessible.contains(letter)) {
                    accessible.add(letter);
                    // If it's a non-terminal, recursively explore it
                    if (grammar.getV_N().contains(letter)) {
                        findAccessibleSymbols(letter, accessible, visited);
                    }
                }
            }
        }
    }

    public void simplify() {

    }

    /**
     * Checks if an RHS is valid for CNF: either one terminal or two non-terminals.
     */
    private boolean isValidCnfRhs(List<Letter> rhs) {
        return true;
    }
}
