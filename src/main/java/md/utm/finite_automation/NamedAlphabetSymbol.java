package md.utm.finite_automation;

import md.utm.grammar.AlphabetSymbol;

public class NamedAlphabetSymbol implements AlphabetSymbol {

    String name;

    public NamedAlphabetSymbol(String name) {
        this.name = name;
    }

    @Override
    public String getAlphabetSymbolName() {
        return name;
    }

    public String toString() {
        return getAlphabetSymbolName();
    }
}
