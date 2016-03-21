package ar.fiuba.tdd.template.tp0;

import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegExGeneratorTest {

    private boolean validate(String regEx, int numberOfResults) {
        RegExGenerator generator = new RegExGenerator(5);
        List<String> results = generator.generate(regEx, numberOfResults);
        // force matching the beginning and the end of the strings
        Pattern pattern = Pattern.compile("^" + regEx + "$");
        return results
                .stream()
                .reduce(true,
                    (acc, item) -> {
                        Matcher matcher = pattern.matcher(item);
                        return acc && matcher.find();
                    },
                    (item1, item2) -> item1 && item2);
    }


    @Test
    public void testAnyCharacter() {
        assertTrue(validate(".", 1));
    }

    @Test
    public void testMultipleCharacters() {
        assertTrue(validate("...", 1));
    }

    @Test
    public void testLiteral() {
        assertTrue(validate("\\@", 1));
    }

    @Test
    public void testLiteralDotCharacter() {
        assertTrue(validate("\\@..", 1));
    }

    @Test
    public void testZeroOrOneCharacter() {
        assertTrue(validate("\\@.h?", 1));
    }

    @Test
    public void testCharacterSet() {
        assertTrue(validate("[abc]", 1));
    }

    @Test
    public void testCharacterSetWithQuantifiers() {
        assertTrue(validate("[abc]+", 1));
    }

    @Test
    public void testCharacterSetWithQuantifierZeroOrMany() {
        assertTrue(validate("[abc]*", 1));
    }

    @Test
    public void testTwoStringsOfCharacterSetWithQuantifiers() {
        assertTrue(validate("[abc]+", 2));
    }

    @Test
    public void testAllKindOfToken() {
        assertTrue(validate("[abc]+t*rhi?\\[*", 2));
    }

    @Test
    public void testEmptyExpression() {
        assertTrue(validate("", 3));
    }

    @Test
    public void testLotOfLiterals() {
        assertTrue(validate("a+b+c+d+e+", 2));
    }

    @Test
    public void testFiveFullRandomFourCharacterWords() {
        assertTrue(validate("....", 5));
    }

    @Test
    public void testTwoSetOfCharacters() {
        assertTrue(validate("[abc]+[def]+a*", 4));
    }

    @Test
    public void testSetOfOneChar() {
        assertTrue(validate("[a]+", 4));
    }



}
