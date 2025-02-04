package lab1;

import md.utm.lab1.Letter;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLetter {

    @Test
    void testSet() {
        Set<Letter> set = new HashSet<>();
        set.add(new Letter("a", true));
        set.add(new Letter("a", true));
        assertEquals(set.size(), 1);
    }



}
