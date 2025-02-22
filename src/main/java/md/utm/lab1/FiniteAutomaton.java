package md.utm.lab1;

import md.utm.lab2.FiniteAutomataToGrammarService;
import md.utm.lab2.FiniteAutomationType;
import md.utm.lab2.VisualisationService;

import java.util.List;
import java.util.Set;

public class FiniteAutomaton {

    private final Set<State> Q;
    private final Set<AlphabetSymbol> sigmaAlphabet; // Alphabet symbols
    private final Set<Transition> deltaTransitions;
    private final State q0;
    private final Set<State> F;


    public FiniteAutomaton(Set<State> Q, Set<AlphabetSymbol> sigmaAlphabet, Set<Transition> deltaTransitions, State q0, Set<State> F) {
        this.Q = Q;
        this.q0 = q0;
        this.sigmaAlphabet = sigmaAlphabet;
        this.deltaTransitions = deltaTransitions;
        this.F = F;
    }

    @Override
    public String toString() {
        return String.format(
                "States: %s%nAlphabet: %n%s%ndelta: %s%nInitial state: %s%nFinal state: %s",
                Q, sigmaAlphabet, deltaTransitions, q0, F
        );
    }

    public void visualize() {
        VisualisationService.visualise(this);
    }

    public boolean belongsToAutomation(List<? extends AlphabetSymbol> word) {
        State currentState = q0;

        for (AlphabetSymbol symbol : word) {
            boolean transitionFound = false;

            for (Transition transition : deltaTransitions) {
                if (transition.getFrom().equals(currentState) && transition.getLabel().equals(symbol)) {
                    currentState = transition.getTo();
                    transitionFound = true;
                    break;
                }
            }
            if (!transitionFound) {
                return false;
            }
        }

        return F.contains(currentState);
    }

    public Grammar toGrammar() {
        return FiniteAutomataToGrammarService.toRegularGrammar(this);
    }

    public FiniteAutomationType getFiniteAutomationType() {
        return FiniteAutomationType.NFA;
    }

    public FiniteAutomaton toDfa() {
        if (getFiniteAutomationType() == FiniteAutomationType.DFA) {
            System.out.println("The Finite Automaton is already a NFA. Returning the same automaton.");
            return this;
        }
        else {
            // implement some logic
            return null;
        }
    }

    public Set<State> getQ() {
        return Q;
    }

    public Set<AlphabetSymbol> getSigmaAlphabet() {
        return sigmaAlphabet;
    }

    public Set<Transition> getDeltaTransitions() {
        return deltaTransitions;
    }

    public State getQ0() {
        return q0;
    }

    public Set<State> getF() {
        return F;
    }
}
