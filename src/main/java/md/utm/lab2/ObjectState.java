package md.utm.lab2;

import md.utm.lab1.State;

import java.util.Objects;

/**
 * Kinda of dynamically typed state. Accepts any object as value. Highly relies on overridden by "core" class method toString();
 */
public class ObjectState<T> implements State {

    private final T value;

    public ObjectState(T value) {
        this.value = value;
    }

    T getValue() {
        return value;
    }

    @Override
    public String getStateName() {
        return value == null ? "<null>" : value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectState<?> that = (ObjectState<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equalStates(State state) {
        return State.super.equalStates(state);
    }

    public String toString() {
        return getStateName();
    }

}
