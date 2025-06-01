package lab5;

import md.utm.chomsky_normal_form.CNFService;
import md.utm.grammar.Grammar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IntermediateNormalizationTest {


    @Test
    public void testSNormalization() {
        Grammar g = VariantGrammarToNormalize.get15();
        CNFService cnf = new CNFService(g);
        cnf.resolveStartingSymbol();

        // initial grammar has S on right
        assertTrue(cnf.hasSOnRight(g.getS(), g.getP()));

        // final grammar doesn't have S on Right
        Grammar cnfGrammar = cnf.getGrammar();
        assertFalse(cnf.hasSOnRight(cnfGrammar.getS(), cnfGrammar.getP()));

    }
}
