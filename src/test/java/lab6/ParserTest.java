package lab6;

import md.utm.parser.AionParser;
import md.utm.tokenizer.Token;
import md.utm.tokenizer.TokenStreamVisualizer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static md.utm.tokenizer.AionLexer.tokenize;
import static md.utm.utils.FileReader.read;

public class ParserTest {

    @Test
    public void testParser() {

        AionParser parser = new AionParser(tokenize(read("D:\\src\\utm\\tasks\\lfa\\labs\\src\\main\\java\\md\\utm\\tokenizer\\resources\\simplest.aion")));

        System.out.println(parser.parse());

    }
}
