package md.utm.chomsky_normal_form;

import md.utm.grammar.Letter;

import java.util.HashSet;
import java.util.Set;

import static md.utm.utils.SubscriptConverter.toSubscript;

public class VariableFactory {

    private final Set<Letter> managedNonterminalLetters;
    private final Set<Letter> managedTerminalLetters;


    public VariableFactory(Set<Letter> terminalLetters, Set<Letter> nonTerminalLetters) {
        this.managedNonterminalLetters = new HashSet<>(nonTerminalLetters);
        this.managedTerminalLetters = new HashSet<>(terminalLetters);
    }

    
    public Letter getNonterminalNewLetter(Letter terminal) {


        // check if it's terminal
        if (managedTerminalLetters.contains(terminal)) {
            // if it's something like c, then we'll try to return C
            if (terminal.getLetter().length() == 1) {
                
                Letter uppercaseLetter = new Letter(terminal.getLetter().toUpperCase());
                
                if (!managedTerminalLetters.contains(uppercaseLetter) && !managedNonterminalLetters.contains(uppercaseLetter)) {
                    managedNonterminalLetters.add(uppercaseLetter);
                    return uppercaseLetter;
                }
                
                else return getNextNonterminal();
            } else return getNextNonterminal();

        } else {
            return null;
        }
    }

    public Letter getNewStartLetter() {
        if (!managedNonterminalLetters.contains(new Letter("S₀"))) {
            Letter newStartLetter = new Letter("S₀");
            managedNonterminalLetters.add(newStartLetter);
            return newStartLetter;
        }
        else throw new RuntimeException(
                "S₀ already exists. You’re not supposed to have a state called S0 because it's typically reserved as the new start state during normalization or automata transformations. " +
                        "Having it already defined risks breaking the construction process, leading to ambiguous behaviors or invalid automata. " +
                        "Seriously, what were you thinking? Were you trying to speedrun breaking my algorithm, or is chaos just your coding style?"
        );

    }

    /**
     * Helper method for generating a letter which do not exist in current <code>Managed Non-terminal letter</code>
     * @return a next available letter
     */
    public Letter getNextNonterminal() {

        for (int j = 0; true; j++) {
            
            // only generates X1, Y11, etc
            for (int i = 'Z'; i >= 'N'; i--) {
                if (i == 'S') continue;
                String newLetterValue = j == 0 ? String.valueOf((char) i) : String.format("%c%s", i, toSubscript(j));

                Letter newLetter = new Letter(newLetterValue);
                if (!managedNonterminalLetters.contains(newLetter) && !managedTerminalLetters.contains(newLetter)) {
                    managedNonterminalLetters.add(newLetter);
                    return newLetter;
                }
            }

        }
    }

}
