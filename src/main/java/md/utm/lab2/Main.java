package md.utm.lab2;

import md.utm.lab1.AlphabetSymbol;
import md.utm.lab1.FiniteAutomaton;
import md.utm.lab1.State;
import md.utm.lab1.Transition;

import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        State q0 = new NamedState("q0");
        State q1 = new NamedState("q1");
        State q2 = new NamedState("q3");
        State q3 = new NamedState("q3");

        var a = new NamedAlphabetSymbol("a");
        var b = new NamedAlphabetSymbol("b");

        Set<State> Q = new HashSet<>(Set.of(q0,q1,q2,q3));
        Set<AlphabetSymbol> sigma = new HashSet<>(Set.of(a,b));


        Set<Transition> delta = new HashSet<>(Set.of(
                new Transition(q0, a, q0),
                new Transition(q0, b, q1),
                new Transition(q1, a, q1),
                new Transition(q1, a, q2),
                new Transition(q1, b, q3),
                new Transition(q2, a, q2),
                new Transition(q2, b, q3)
        ));

        FiniteAutomaton finiteAutomaton = new FiniteAutomaton(Q, sigma, delta, q0, Set.of(q3));
        System.out.println(finiteAutomaton);

    }


}
