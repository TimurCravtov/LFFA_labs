package md.utm.lab1;

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
        return STR."States: \{Q}\nAlphabet: \n\{sigmaAlphabet}\ndelta: \{deltaTransitions}\nInitial state: \{q0}\nFinal state: \{F}";
    }

    public void visualize(String outputDirectory) {
        VisualisationService.visualise(this, outputDirectory);
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
