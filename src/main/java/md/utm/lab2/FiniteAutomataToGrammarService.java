package md.utm.lab2;

import md.utm.lab1.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FiniteAutomataToGrammarService {
    public static Grammar toRegularGrammar(FiniteAutomaton finiteAutomaton) {
        // Create non-terminal symbols from states
        Set<Letter> V_N = finiteAutomaton.getQ().stream()
                .filter(Predicate.not(l -> l.equalStates(Letter.F)))
                .map(q -> new Letter(q.getStateName()))
                .collect(Collectors.toSet());

        // Create terminal symbols from alphabet
        Set<Letter> V_T = finiteAutomaton.getSigmaAlphabet().stream()
                .map(e -> new Letter(e.getAlphabetSymbolName()))
                .collect(Collectors.toSet());

        // Start symbol is the initial state
        Letter S = new Letter(finiteAutomaton.getQ0().getStateName());
        Set<DeriveRule> P = new HashSet<>();

        // Create production rules from transitions
        for (Transition transition : finiteAutomaton.getDeltaTransitions()) {
            Letter fromState = new Letter(transition.getFrom().getStateName());
            Letter symbol = new Letter(transition.getLabel().getAlphabetSymbolName());
            Letter toState = new Letter(transition.getTo().getStateName());

            // If the transition leads to a final state, add a production rule A -> b
            if (finiteAutomaton.getF().contains(transition.getTo())) {
                P.add(new DeriveRule(fromState, symbol));
            } else {
                P.add(new DeriveRule(fromState, List.of(symbol, toState)));
            }
        }

        return new Grammar(V_N, V_T, P, S);
    }
}