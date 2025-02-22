package md.utm.lab1;

public class Transition {
    private State from;
    private AlphabetSymbol label;
    private State to;

    public Transition(State from, AlphabetSymbol label, State to) {
        this.from = from;
        this.label = label;
        this.to = to;
    }

    public State getFrom() {
        return from;
    }

    public void setFrom(State from) {
        this.from = from;
    }

    public AlphabetSymbol getLabel() {
        return label;
    }

    public void setLabel(AlphabetSymbol label) {
        this.label = label;
    }

    public State getTo() {
        return to;
    }

    public void setTo(Letter to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format("δ(%s, %s) = %s", from.getStateName(), label.getAlphabetSymbolName(), to.getStateName());
    }

}
