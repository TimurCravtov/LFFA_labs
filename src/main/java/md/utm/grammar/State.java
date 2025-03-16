package md.utm.grammar;

import java.util.Objects;

public interface State {

    String getStateName();

    default boolean equalStates(State state) {
        return Objects.equals(this.getStateName(), state.getStateName());
    }
}
