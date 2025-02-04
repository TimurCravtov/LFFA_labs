package md.utm.lab1;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Grammar {

    // non-terminal letters
    private final Set<Letter> V_N;

    // terminal letters
    private final Set<Letter> V_T;

    // productions
    private final Set<DeriveRule> P;

    // start letter
    private final Letter S;

    public Grammar(Set<Letter> V_N, Set<Letter> V_T, Set<DeriveRule> productions, Letter S) {

        if (Stream.of(V_T, V_N, S, productions).anyMatch(Objects::isNull)) {
            throw new RuntimeException("V_T, V_N, S and P should not be null");
        }

        this.V_N = V_N;
        this.V_T = V_T;
        this.P = productions;
        this.S = S;
    }

    public FiniteAutomaton toFiniteAutomation() {
        return null;
    }


    /**
     * This method generates random string based on grammar starting with S. While there are not-terminal symbols, it
     * <p>
     * 1) Picks the random non-terminal letter
     * <p>
     * 2) Picks the random derive rules for this letter.
     * <p>
     * 3) Replaces the letter
     * <p>
     * 4) Repeats
     * @param showProcess if you need to see the steps of word generation, pass <i>true</i>
     * @return generated string
     */
    public String generateRandomString(boolean showProcess) {
        System.out.println(STR."1. \{S}");
        int i = 0;

        List<Letter> word = new ArrayList<>(List.of(S));
        Random random = new Random();

        while (word.stream().anyMatch(letter -> !letter.isTerminal())) {

            List<Letter> nextGenWord = new ArrayList<>(word);
            List<Letter> finalWord = word; // intelij says you can't use not final in lambda :(

            List<Integer> nonTerminalIndices = IntStream.range(0, word.size())
                    .filter(index -> !finalWord.get(index).isTerminal())
                    .boxed()
                    .toList();

            if (!nonTerminalIndices.isEmpty()) {

                int randomIndex = nonTerminalIndices.get(random.nextInt(nonTerminalIndices.size()));
                Letter randomLetter = word.get(randomIndex);

                List<DeriveRule> possibleRules = P.stream()
                        .filter(rule -> rule.getFrom().getFirst().equals(randomLetter))
                        .toList();

                if (!possibleRules.isEmpty()) {
                    DeriveRule selectedRule = possibleRules.get(random.nextInt(possibleRules.size()));

                    nextGenWord.remove(randomIndex);
                    nextGenWord.addAll(randomIndex, selectedRule.getTo());
                }
                else {
                    throw new RuntimeException(STR."Couldn't find right rule for letter \{randomLetter} adjust your productions or alphabet");
                }
            }

            word = nextGenWord;
            i++;

            if (showProcess) {
                System.out.println(STR."\{i}. \{word.stream().map(Letter::getLetter).collect(Collectors.joining())}");
            }
        }

        return word.stream()
                .map(Letter::getLetter)
                .collect(Collectors.joining());
    }
}
