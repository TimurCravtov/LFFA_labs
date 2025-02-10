package md.utm.lab2;

import md.utm.lab1.AlphabetSymbol;

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
