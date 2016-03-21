package ar.fiuba.tdd.template.tp0;

import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RegExGenerator {

    private int maxLength;

    public RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
    }


    public List<String> generate(String regEx, int numberOfResults) {
        ArrayList<String> arrayToReturn = new ArrayList<String>();
        for (int i = 0 ; i < numberOfResults ; i++) {
            arrayToReturn.add(this.getWordForRegEx(regEx));
        }
        System.out.println(arrayToReturn);
        return arrayToReturn;
    }

    private String getWordForRegEx(String regEx) {
        String wordToReturn = "";
        StringCharacterIterator stringIterator = new StringCharacterIterator(regEx);
        char actualCharInExpression = stringIterator.first();
        while (actualCharInExpression != stringIterator.DONE) {
            switch (actualCharInExpression) {
                case '.':
                    wordToReturn = wordToReturn + this.processDotToken(stringIterator);
                    actualCharInExpression = stringIterator.current();
                    break;
                case '[':
                    wordToReturn = wordToReturn + this.processSetToken(stringIterator);
                    actualCharInExpression = stringIterator.current();
                    break;
                case '\\':
                    wordToReturn = wordToReturn + this.processBackslashToken(stringIterator);
                    actualCharInExpression = stringIterator.current();
                    break;
                default:
                    wordToReturn = wordToReturn + this.processLiteralToken(stringIterator);
                    actualCharInExpression = stringIterator.current();
                    break;
            }
        }
        return wordToReturn;
    }

    private String processDotToken(StringCharacterIterator iterator){
        String stringToReturn = "";
        char currentChar = iterator.next();
        if (currentChar != iterator.DONE) {
            if (this.isAQuantifierChar(currentChar)) {
                stringToReturn = stringToReturn + this.stringForDotToken(currentChar);
                iterator.next();
            } else {
                stringToReturn = stringToReturn + this.stringForDotToken('\0');
            }
        } else {
            stringToReturn = stringToReturn + this.stringForDotToken('\0');
        }
        return stringToReturn;
    }

    private String processSetToken(StringCharacterIterator iterator){
        String stringToReturn = "";
        char currentChar = iterator.next();
        ArrayList<Character> setOfPossibleChars =  this.getSetOfChars(iterator);
        currentChar = iterator.next();
        if (this.isAQuantifierChar(currentChar)) {
            stringToReturn = stringToReturn + this.stringWithSetToken(setOfPossibleChars, currentChar);
            iterator.next();
        } else {
            stringToReturn = stringToReturn + this.stringWithSetToken(setOfPossibleChars, '\0');
        }
        return stringToReturn;
    }

    private String processBackslashToken(StringCharacterIterator iterator){
        String stringToReturn = "";
        char currentChar = iterator.next();
        char escapedChar = currentChar;
        currentChar = iterator.next();
        if (this.isAQuantifierChar(currentChar)) {
            stringToReturn = stringToReturn + this.stringForLiteral(escapedChar, currentChar);
            iterator.next();
        } else {
            stringToReturn = stringToReturn + this.stringForLiteral(escapedChar, '\0');
        }
        return stringToReturn;
    }

    private String processLiteralToken(StringCharacterIterator iterator){
        String stringToReturn = "";
        char literal = iterator.current();
        char currentChar = iterator.next();
        if (this.isAQuantifierChar(currentChar)) {
            stringToReturn = stringToReturn + this.stringForLiteral(literal, currentChar);
            iterator.next();
        } else {
            stringToReturn = stringToReturn + this.stringForLiteral(literal, '\0');
        }
        return stringToReturn;
    }

    private ArrayList<Character> getSetOfChars(StringCharacterIterator iterator){
        char actualChar = iterator.next();
        ArrayList<Character> arrayToReturn = new ArrayList<Character>();
        while (actualChar != ']') {
            if (actualChar == '\\') {
                actualChar = iterator.next();
                arrayToReturn.add(actualChar);
            } else if (actualChar != iterator.DONE) {
                arrayToReturn.add(actualChar);
            } else {
                return new ArrayList<Character>() {
                    {
                        add('\0');
                    }
                };
            }
            actualChar = iterator.next();
        }
        return arrayToReturn;
    }
    private boolean isAQuantifierChar(char regularExpressionChar) {
        if (regularExpressionChar == '*' || regularExpressionChar == '?' || regularExpressionChar == '+') {
            return true;
        } else {
            return false;
        }

    }


    private String stringForLiteral(char literal , char quantifierChar) {
        String stringToReturn = "";
        int stringLength = this.getLengthForQuantifier(quantifierChar);
        Random randomizer = new Random();
        for (int i = 0 ; i < stringLength; i++) {
            stringToReturn = stringToReturn + literal;
        }
        return stringToReturn;
    }

    private String stringWithSetToken(ArrayList<Character> setOfChars, char quantifierChar) {
        String stringToReturn = "";
        int lengthToReturn = this.getLengthForQuantifier(quantifierChar);
        int numberOfCharsInSet = setOfChars.size();
        Random randomizer = new Random();

        for (int i = 0; i < lengthToReturn; i++) {
            int randomSetPosition = randomizer.nextInt(numberOfCharsInSet - 1);
            stringToReturn = stringToReturn + setOfChars.get(randomSetPosition);
        }

        return stringToReturn;
    }

    private int getLengthForQuantifier(char quantifierChar) {
        int lengthToReturn = 0;
        Random randomizer = new Random();
        if (quantifierChar == '\0') {
            lengthToReturn = 1;
        } else {
            switch (quantifierChar) {
                case '?':
                    lengthToReturn = randomizer.nextInt(1);
                    break;
                case '*':
                    lengthToReturn = randomizer.nextInt(this.maxLength);
                    break;
                case '+':
                    lengthToReturn = 1 + randomizer.nextInt(this.maxLength);
                    break;
                default:
                    lengthToReturn = 0;
                    break;
            }

        }

        return lengthToReturn;
    }

    private String stringForDotToken(char quantifierChar) {
        String stringToReturn = "";
        Random randomizer = new Random();
        int stringLongitude;
        stringLongitude = this.getLengthForQuantifier(quantifierChar);

        for (int i = 0; i < stringLongitude ; i++) {
            int randomNumber = 33 + randomizer.nextInt(126 - 33); // getting a random number between 33 and 126
            stringToReturn = stringToReturn + (char) randomNumber;
        }
        return stringToReturn;
    }
}