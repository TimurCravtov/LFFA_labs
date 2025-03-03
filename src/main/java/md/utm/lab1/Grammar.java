package md.utm.lab1;

import md.utm.lab2.ChomskyType;

import java.util.*;
import java.util.function.Predicate;
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
    private ChomskyType type;


    public Grammar(Set<Letter> V_N, Set<Letter> V_T, Set<DeriveRule> productions, Letter S) {


        if (Stream.of(V_T, V_N, S, productions).anyMatch(Objects::isNull)) {
            throw new RuntimeException("V_T, V_N, S and P should not be null");
        }

        Set<Letter> total = new HashSet<>(Set.copyOf(V_N));
        total.addAll(V_T);

        if (total.size() != V_T.size() + V_N.size()) {
            throw new RuntimeException("V_T and V_N should not have common elements");
        }

//        Collections.disjoint(V_N, productions.stream().map(rule -> rule.getFrom().stream().toList()));

        for (DeriveRule rule: productions) {
            if (rule.getFrom().contains(V_N));
        }

        this.V_N = V_N;
        this.V_T = V_T;
        this.P = productions;
        this.S = S;
    }

    /**
     * The classification of grammar works the following way:
     * <br>
     * 1) Filters the rules by the 'from' part.
     * <br>
     * 2) If the set's size is 0, it's either third or second type
     * <br>
     * 3) To find out, the filter of regularity happens
     * <br>
     * 4) If some elements left from the 2. step, the next filter is performed: context-sensitive one
     * <br>
     * 5) If some elements left, it's the 0th type
     * @return Chomsky type of the grammar
     */
    public ChomskyType getChomskyType(boolean reasoning) {


        Predicate<DeriveRule> beforeTransitionOneLetterFilter = rule -> rule.getFrom().size() == 1 && V_N.contains(rule.getFrom().getFirst());

        Predicate<DeriveRule> afterTransitionRegularityFilter = rule -> {

            List<Integer> terminalIndexes = IntStream.range(0, rule.getFrom().size())
                    .filter(index -> V_T.contains(rule.getFrom().get(index)))
                    .boxed()
                    .toList();

            // either the transition is in the form T -> xxxewerewer

            if (terminalIndexes.isEmpty()) {
                return true;
            }

            // or there is T -> Tmdsfksmd - one terminal, and it's either the first or the last symbol
            return terminalIndexes.size() == 1 && Set.of(0, rule.getFrom().size() - 1).contains(terminalIndexes.getFirst());
        };

        Predicate<Collection<DeriveRule>> sameRegularitySide = rules -> {
            boolean hasLeftRegular = false;
            boolean hasRightRegular = false;

            for (DeriveRule rule : rules) {
                List<Letter> to = rule.getTo();

                // Skip rules with only terminals
                if (V_T.containsAll(to)) {
                    continue;
                }

                // Find position of non-terminal
                int nonTerminalIndex = to.indexOf(to.stream()
                        .filter(V_N::contains)
                        .findFirst()
                        .orElse(null));

                // Left-regular: non-terminal at the end
                if (nonTerminalIndex == to.size() - 1) {
                    hasLeftRegular = true;
                }
                // Right-regular: non-terminal at the start
                else if (nonTerminalIndex == 0) {
                    hasRightRegular = true;
                }

                if (hasLeftRegular && hasRightRegular) {
                    return false;
                }
            }

            return true;
        };


        // main action happens from here

        Set<DeriveRule> firstStep =  new HashSet<>(P);

        // translation to human: second step - true - holds all the rules where the 'from' part is simply one letter
        Map<Boolean, List<DeriveRule>> secondStepMap = firstStep.stream().collect(Collectors.partitioningBy(beforeTransitionOneLetterFilter));

        if (reasoning) System.out.println(secondStepMap);
        if (secondStepMap.get(false).isEmpty()) {
            if (reasoning) System.out.println("The second step which devided the rules into the 'the before part has one symbol' and 'the before part has more than one symbol' concluded that all the rules were in form A->something, so it's either TYPE2 or TYPE3");

            Set<DeriveRule> secondStep = new HashSet<>(secondStepMap.get(true));


            // third step: all the rules from the second step which are regular
            Set<DeriveRule> thirdStep = secondStep.stream().filter(afterTransitionRegularityFilter).collect(Collectors.toSet());

            if (thirdStep.size() < secondStep.size()) {
                if (reasoning) System.out.println("After the filter of regularity in 'to' part, some rules are defined as not regular. That means, the type is TYPE-2 ");
                return ChomskyType.TYPE2;
            }

            // this else is: third step size == second step size => all the rules are regular.
            else {

                // now we check the regularity side: if the rules are or right regular or left regular, not mixed
                if (sameRegularitySide.test(thirdStep)) {
                    if (reasoning) {
                        System.out.println(thirdStep);

                    System.out.println("The same regularity test showed true. Returning that the type is TYPE-3");
                    }
                    return ChomskyType.TYPE3;
                } else {
                    if (reasoning) System.out.println("The same regularity test showed false. Returning that the type is TYPE-2");
                    return ChomskyType.TYPE2;
                }
            }

        }
        boolean isContextSensitive = true;

        for (DeriveRule rule : secondStepMap.get(false)) {
            if (rule.getFrom().size() > rule.getTo().size()) {
                isContextSensitive = false;
                break;
            }
        }

        boolean hasSEpsilonRule = P.stream()
                .anyMatch(rule -> rule.getFrom().size() == 1 && rule.getFrom().getFirst().equals(S) && rule.getTo().getFirst().equals(Letter.EPSILON));

        // idk what for is this, might be useful
        if (hasSEpsilonRule) {
            if (reasoning) System.out.println("The grammar contains the rule S -> ε, which is allowed in context-sensitive grammars.");
        }

        if (isContextSensitive) {
            if (reasoning) System.out.println("All rules satisfy the context-sensitive condition. The grammar is Type 1.");
            return ChomskyType.TYPE1;
        } else {
            if (reasoning) System.out.println("Some rules violate the context-sensitive condition. The grammar is Type 0.");
            return ChomskyType.TYPE0;
        }
    }

    public boolean isRegular() {
        return getChomskyType(false) == ChomskyType.TYPE3;
    }

    public FiniteAutomaton toFiniteAutomation() {

        if (!isRegular()) {
            throw new RuntimeException("The grammar is not regular, can't create finite automata");
        }

        Set<Transition> delta = new HashSet<>();

        for (DeriveRule rule : P) {
            Letter first = rule.getFrom().getFirst(); // for sure it's just one

            List<Letter> to = rule.getTo();
            if (to.size() == 1) {
                delta.add(new Transition(first, to.getFirst(), Letter.F));
            }
            else if (to.size() > 2) {
                throw new RuntimeException("We don't know how to deal with exptended grammar in FA contruction");
            }
            else {

                Optional<Letter> terminalOpt = to.stream().filter(V_T::contains).findFirst();

                int terminalIndex = terminalOpt
                        .map(to::indexOf)
                        .orElseThrow(() -> new RuntimeException("We don't know how to deal with extended grammar in FA construction"));

                delta.add(
                        new Transition(first, to.get(terminalIndex), to.get(1 - terminalIndex))
                );
            }

        }

        Set<State> states = new HashSet<>(V_N);
        Set<AlphabetSymbol> alphabet = new HashSet<>(V_T);
        states.add(Letter.F);

        return new FiniteAutomaton(states, alphabet, delta, S, Set.of(Letter.F));
    }

    public String toString() {
        return String.format("V_N : %s%nV_T: %s%nS: %s%nP: %s", V_N, V_T, S, P);    }


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
            System.out.println(String.format("1. %s", S));
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
                    throw new RuntimeException(String.format("Couldn't find right rule for letter %s, adjust your productions or alphabet", randomLetter));
                }
            }

            word = nextGenWord;
            i++;

            if (showProcess) {
                System.out.println(String.format("%d. %s", i, word.stream().map(Letter::getLetter).collect(Collectors.joining())));
            }
        }

        return Word.makeString(word);
    }
}
