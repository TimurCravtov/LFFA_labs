package md.utm.chomsky_normal_form;

import md.utm.grammar.Letter;

import java.util.HashSet;
import java.util.Set;

public class VariableFactory {

    private final Set<Letter> managedNonterminalLetters;
    private final Set<Letter> managedTerminalLetters;


    public VariableFactory(Set<Letter> terminalLetters, Set<Letter> nonTerminalLetters) {
        this.managedNonterminalLetters = new HashSet<>(terminalLetters);
        this.managedTerminalLetters = new HashSet<>(nonTerminalLetters);
    }

    public Letter getNewLetter(Letter letter) {

        // check if it's terminal
        if (managedTerminalLetters.contains(letter)) {

            Letter uppercaseLetter = new Letter(letter.getLetter().toUpperCase());
            if (!managedTerminalLetters.contains(uppercaseLetter) && !managedNonterminalLetters.contains(uppercaseLetter)) {
                managedNonterminalLetters.add(uppercaseLetter);
                return uppercaseLetter;
            }
            else return getNext();
        }
        else return null;
    }

    public Letter getNewStartLetter() {
        if (!managedNonterminalLetters.contains(new Letter("S0"))) {
            Letter newStartLetter = new Letter("S0");
            managedNonterminalLetters.add(newStartLetter);
            return newStartLetter;
        }
        else throw new RuntimeException(
                "S0 already exists. You’re not supposed to have a state called S0 because it's typically reserved as the new start state during normalization or automata transformations. " +
                        "Having it already defined risks breaking the construction process, leading to ambiguous behaviors or invalid automata. " +
                        "Seriously, what were you thinking? Were you trying to speedrun breaking my algorithm, or is chaos just your coding style?"
        );

    }

    public Letter getNext() {

        for (int j = 0; true; j++) {
            for (int i = 'Z'; i >= 'X'; i--) {

                String newLetterValue = j == 0 ? String.valueOf((char) i) : String.format("%c%d", i, j);
                Letter newLetter = new Letter(newLetterValue);
                if (!managedNonterminalLetters.contains(newLetter)) {
                    managedNonterminalLetters.add(newLetter);
                    return newLetter;
                }
            }

        }
    }

}
