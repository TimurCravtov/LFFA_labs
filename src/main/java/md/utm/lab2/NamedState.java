package md.utm.lab2;

import md.utm.lab1.State;

public class NamedState implements State {

    String name;

    public NamedState(String name) {
        this.name = name;
    }

    public String getStateName() {
        return name;
    }

    @Override
    public boolean equalStates(State state) {
        return State.super.equalStates(state);
    }

    public String toString() {
        return getStateName();
    }
}
