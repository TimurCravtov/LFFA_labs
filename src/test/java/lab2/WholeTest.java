package lab2;

import md.utm.grammar.FiniteAutomaton;
import md.utm.grammar.Grammar;
import md.utm.grammar.Main;
import md.utm.finite_automation.ChomskyType;
import md.utm.finite_automation.FiniteAutomationType;
import org.junit.jupiter.api.Test;

import static md.utm.finite_automation.Main.getLabTwoAutomata;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WholeTest {

    @Test
    public void testGrammarType() {
        Grammar grammarOneLab = md.utm.grammar.Main.getLabOneGrammar();

        System.out.println("============= Lab 1 Grammar: =============== \n" + grammarOneLab);

        ChomskyType chomskyType = grammarOneLab.getChomskyType(false);
        System.out.println("Chomsky type of Lab1 grammar: " + chomskyType);
        assertEquals("Type-3", chomskyType.toString());
    }


    @Test
    public void testFiniteAutomatonType() {

        FiniteAutomaton finiteAutomaton = getLabTwoAutomata();

        System.out.println("============= Lab 2 F.A. : =============== \n" + finiteAutomaton);

        finiteAutomaton.visualize();
        FiniteAutomationType finiteAutomationType = finiteAutomaton.getFiniteAutomationType();

        System.out.println("The type of finite automaton: " + finiteAutomationType);
        assertEquals("NFA", finiteAutomationType.toString());

        Grammar faEq = finiteAutomaton.toGrammar();
//        System.out.println(faEq.getChomskyType(false));
        System.out.println("====== Its equivalent Grammar: \n " + faEq);
    }

    @Test
    public void testNFAtoDFAConversion() {
        FiniteAutomaton finiteAutomaton = getLabTwoAutomata();
        System.out.println("============= Lab 2 F.A. : =============== \n" + finiteAutomaton);
        System.out.println("Its equivalent DFA: \n " + finiteAutomaton.toDfa());
        finiteAutomaton.toDfa().visualize();
    }

    @Test
    public void biTest() {
        Grammar grammar1 = Main.getLabOneGrammar();
        System.out.println("Grammar : \n" + grammar1);

        FiniteAutomaton finiteAutomaton = grammar1.toFiniteAutomation();
        System.out.println("Grammar to Finite Automation : \n" + finiteAutomaton);

        Grammar grammar2 = finiteAutomaton.toGrammar();

        System.out.println("Finite Automation to grammar: \n" + grammar2);

    }

}
