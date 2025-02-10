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

        // todo: check if in productions 'from' has at least one non-terminal symbol
        if (Stream.of(V_T, V_N, S, productions).anyMatch(Objects::isNull)) {
            throw new RuntimeException("V_T, V_N, S and P should not be null");
        }

        this.V_N = V_N;
        this.V_T = V_T;
        this.P = productions;
        this.S = S;
    }

    public boolean isRegular() {
        for (DeriveRule rule : P) {
            List<Letter> from = rule.getFrom();
            List<Letter> to = rule.getTo();

            if (from.size() != 1 || !V_N.contains(from.getFirst())) {
                return false;
            }

            if (to.isEmpty() || !V_T.contains(to.get(0))) {
                return false;
            }

            if (to.size() > 1 && (!V_N.contains(to.get(1)) || to.size() > 2)) {
                return false;
            }
        }
        return true;
    }

    public FiniteAutomaton toFiniteAutomation() {

        if (!isRegular()) {
            throw new RuntimeException("The grammar is not regular, can't create finite automata");
        }

        Set<Transition> delta = new HashSet<>();

        // transform rules in transitions
        P.forEach(rule -> delta.add(
                new Transition(
                        rule.getFrom().getFirst(),
                        rule.getTo().getFirst(),
                        rule.getTo().size() == 2 ? rule.getTo().get(1) : Letter.F
                )));

        System.out.println(delta);

        // add to states all the non-terminal plus final
        Set<State> states = new HashSet<>(V_N);
        Set<AlphabetSymbol> alphabet = new HashSet<>(V_T);
        states.add(Letter.F);

        return new FiniteAutomaton(states, alphabet, delta, S, Set.of(Letter.F));
    }

    public String toString() {
        return STR."V_N : \{V_N.toString()}\nV_T: \{V_T}\nS: \{S}\nP:\{P}";
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

        int i = 0;

        if (showProcess) {
            System.out.println(STR."1. \{S}");
        }

        List<Letter> word = new ArrayList<>(List.of(S));
        Random random = new Random();

        while (word.stream().anyMatch(V_N::contains)) {

            List<Letter> nextGenWord = new ArrayList<>(word);
            List<Letter> finalWord = word;

            List<Integer> nonTerminalIndices = IntStream.range(0, finalWord.size())
                    .filter(index -> V_N.contains(finalWord.get(index)))
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

        return Word.makeString(word);
    }
}
