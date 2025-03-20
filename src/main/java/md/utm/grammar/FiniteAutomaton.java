package md.utm.grammar;

import md.utm.finite_automation.FiniteAutomataToGrammarService;
import md.utm.finite_automation.FiniteAutomationType;
import md.utm.finite_automation.ObjectState;
import md.utm.finite_automation.VisualisationService;

import java.util.*;
import java.util.stream.Collectors;

public class FiniteAutomaton {

    private final Set<State> Q;
    private final Set<AlphabetSymbol> sigmaAlphabet;
    private final Set<Transition> deltaTransitions;
    private final State q0;
    private final Set<State> F;
    private FiniteAutomationType finiteAutomatonType;

    public FiniteAutomaton(Set<State> Q,
                           Set<AlphabetSymbol> sigmaAlphabet,
                           Set<Transition> deltaTransitions,
                           State q0,
                           Set<State> F) {
        this.Q = Q;
        this.q0 = q0;
        this.sigmaAlphabet = sigmaAlphabet;
        this.deltaTransitions = deltaTransitions;
        this.F = F;
    }

    @Override
    public String toString() {
        return String.format(
                "States: %s%nAlphabet: %s%ndelta: %s%nInitial state: %s%nFinal state: %s",
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

    public Grammar toGrammar() {
        return FiniteAutomataToGrammarService.toRegularGrammar(this);
    }

    public FiniteAutomationType getFiniteAutomationType() {

        if (finiteAutomatonType == null) {

            Set<String> seenTransitions = new HashSet<>();

            for (Transition transition : deltaTransitions) {
                if (transition.getLabel().equals(AlphabetSymbol.EPSILON)) {
                    System.out.println("Has EPSILON transitions");
                    finiteAutomatonType = FiniteAutomationType.NFA;
                    return finiteAutomatonType;

                }

                // Create a unique identifier for each (state, symbol) pair
                String key = transition.getFrom() + ":" + transition.getLabel();

                // If a duplicate is found, it's an NFA
                if (!seenTransitions.add(key)) {
                    finiteAutomatonType = FiniteAutomationType.NFA;
                    return finiteAutomatonType;
                }
            }
            finiteAutomatonType = FiniteAutomationType.DFA;
        }

        return finiteAutomatonType;

    }



    public FiniteAutomaton toDfa() {
        if (getFiniteAutomationType() == FiniteAutomationType.DFA) {
            System.out.println("The Finite Automaton is already a DFA. Returning the same automaton.");
            return this;
        } else {
            // Initialize the initial state of the DFA with the EPSILON closure of the NFA's initial state
            ObjectState<HashSet<State>> dfaQ0 = new ObjectState<>(new HashSet<>());
            dfaQ0.getObject().add(this.q0);
            dfaQ0.getObject().addAll(getEpsilonClosure(this.q0));

            // Initialize the set of DFA states and transitions
            Set<ObjectState<HashSet<State>>> dfaStates = new HashSet<>();
            dfaStates.add(dfaQ0);

            Set<Transition> dfaTransitions = new HashSet<>();


            // this queue is gonna be used for processing the elements in states of DFA
            Queue<ObjectState<HashSet<State>>> queue = new LinkedList<>();
            queue.add(dfaQ0);

            while (!queue.isEmpty()) {
                ObjectState<HashSet<State>> currentDfaState = queue.poll();

                // For each symbol in the alphabet, determine the next state in the DFA
                for (AlphabetSymbol symbol : sigmaAlphabet) {

                    // find the nfa states reachable from current dfa state
                    Set<State> nextNfaStates = new HashSet<>();
                    for (State nfaState : currentDfaState.getObject()) {
                        nextNfaStates.addAll(this.getNextStates(nfaState, symbol));
                    }


                    Set<State> nextDfaStateSet = new HashSet<>();
                    for (State state : nextNfaStates) {
                        nextDfaStateSet.addAll(getEpsilonClosure(state));
                    }


                    if (!nextDfaStateSet.isEmpty()) {
                        ObjectState<HashSet<State>> nextDfaState = new ObjectState<>(new HashSet<>(nextDfaStateSet));

                        if (!dfaStates.contains(nextDfaState)) {
                            dfaStates.add(nextDfaState);
                            queue.add(nextDfaState);
                        }

                        // Add the transition to the DFA
                        dfaTransitions.add(new Transition(currentDfaState, symbol, nextDfaState));
                    }
                }
            }

            Set<ObjectState<HashSet<State>>> dfaFinalStates = dfaStates.stream()
                    .filter(state -> state.getObject().stream().anyMatch(F::contains))
                    .collect(Collectors.toSet());


            return new FiniteAutomaton(
                    new HashSet<>(dfaStates),
                    sigmaAlphabet,
                    dfaTransitions,
                    dfaQ0,
                    new HashSet<>(dfaFinalStates)
            );
        }
    }

    private Set<State> getEpsilonClosure(State state) {
        Set<State> closure = new HashSet<>();
        closure.add(state);

        Stack<State> stack = new Stack<>();
        stack.push(state);

        while (!stack.isEmpty()) {
            State current = stack.pop();
            for (Transition transition : deltaTransitions) {
                if (transition.getFrom().equals(current) && transition.getLabel().equals(AlphabetSymbol.EPSILON)) {
                    if (!closure.contains(transition.getTo())) {
                        closure.add(transition.getTo());
                        stack.push(transition.getTo());
                    }
                }
            }
        }

        return closure;
    }

    private Set<State> getNextStates(State state, AlphabetSymbol symbol) {
        return deltaTransitions.stream()
                .filter(transition -> transition.getFrom().equals(state) && transition.getLabel().equals(symbol))
                .map(Transition::getTo)
                .collect(Collectors.toSet());
    }
}

