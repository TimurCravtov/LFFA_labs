package lab3;

import md.utm.tokenizer.Token;
import md.utm.tokenizer.TokenStreamVisualizer;
import org.junit.jupiter.api.Test;
import static md.utm.tokenizer.AionLexer.tokenize;
import static md.utm.utils.FileReader.read;


import java.util.List;


public class LexerTest {

    @Test
    public void testSample() {

        String sampleInput = read("D:\\src\\utm\\tasks\\lfa\\labs\\src\\main\\java\\md\\utm\\lab3\\resources\\sample.aion");
        List<Token> tokens = tokenize(sampleInput);
        TokenStreamVisualizer.visualize(tokens);
        System.out.println();
        tokens.forEach(System.out::println);

    }

    @Test
    public void testSimple() {

        String sampleInput = read("D:\\src\\utm\\tasks\\lfa\\labs\\src\\main\\java\\md\\utm\\lab3\\resources\\simple.aion");
        List<Token> tokens = tokenize(sampleInput);
        TokenStreamVisualizer.visualize(tokens);
        System.out.println();
        tokens.forEach(System.out::println);
    }

}
