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
        StringBuilder wordToReturn = new StringBuilder();
        StringCharacterIterator stringIterator = new StringCharacterIterator(regEx);
        char actualCharInExpression = stringIterator.first();
        while (actualCharInExpression != stringIterator.DONE) {
            if (actualCharInExpression == '.') {
                wordToReturn.append(this.processDotToken(stringIterator));
            } else if (actualCharInExpression == '[') {
                wordToReturn.append(this.processSetToken(stringIterator));

            } else if (actualCharInExpression == '\\') {
                wordToReturn.append(this.processBackslashToken(stringIterator));
            } else {
                wordToReturn.append(this.processLiteralToken(stringIterator));
            }
            actualCharInExpression = stringIterator.current();
        }
        return wordToReturn.toString();
    }

    private String processDotToken(StringCharacterIterator iterator) {
        String stringToReturn = "";
        iterator.next();
        stringToReturn = stringToReturn + this.stringForDotToken(iterator);
        return stringToReturn;
    }

    private String processSetToken(StringCharacterIterator iterator) {
        String stringToReturn = "";
        ArrayList<Character> setOfPossibleChars =  this.getSetOfChars(iterator);
        iterator.next();
        stringToReturn = stringToReturn + this.stringWithSetToken(setOfPossibleChars, iterator);

        return stringToReturn;
    }

    private String processBackslashToken(StringCharacterIterator iterator) {
        String stringToReturn = "";
        char currentChar = iterator.next();
        char escapedChar = currentChar;
        iterator.next();

        stringToReturn = stringToReturn + this.stringForLiteral(escapedChar, iterator);

        return stringToReturn;
    }

    private String processLiteralToken(StringCharacterIterator iterator) {
        String stringToReturn = "";
        char literal = iterator.current();
        iterator.next();

        stringToReturn = stringToReturn + this.stringForLiteral(literal, iterator);

        return stringToReturn;
    }

    private ArrayList<Character> getSetOfChars(StringCharacterIterator iterator) {
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


    private String stringForDotToken(StringCharacterIterator iterator) {
        StringBuilder stringToReturn = new StringBuilder();
        Random randomizer = new Random();
        int stringLongitude;

        stringLongitude = this.getLengthForQuantifier(iterator);


        for (int i = 0; i < stringLongitude ; i++) {
            int randomNumber = 33 + randomizer.nextInt(127 - 33); // getting a random number between 33 and 126
            stringToReturn.append((char) randomNumber);
        }
        return stringToReturn.toString();
    }

    private String stringForLiteral(char literal , StringCharacterIterator iterator) {
        StringBuilder stringToReturn = new StringBuilder();
        int stringLength;
        stringLength = this.getLengthForQuantifier(iterator);

        for (int i = 0 ; i < stringLength; i++) {
            stringToReturn.append(literal);
        }
        return stringToReturn.toString();
    }

    private String stringWithSetToken(ArrayList<Character> setOfChars, StringCharacterIterator iterator) {
        StringBuilder stringToReturn = new StringBuilder();
        int lengthToReturn = this.getLengthForQuantifier(iterator);
        int numberOfCharsInSet = setOfChars.size();
        Random randomizer = new Random();
        if (numberOfCharsInSet > 0) {
            for (int i = 0; i < lengthToReturn; i++) {
                int randomSetPosition = randomizer.nextInt(numberOfCharsInSet);
                stringToReturn.append(setOfChars.get(randomSetPosition));
            }
        }

        return stringToReturn.toString();
    }

    private int getLengthForQuantifier(StringCharacterIterator iterator) {
        int lengthToReturn;
        char quantifierChar = iterator.current();
        Random randomizer = new Random();

        if (quantifierChar == '?') {
            lengthToReturn = randomizer.nextInt(2);
            iterator.next();

        } else if (quantifierChar == '*') {
            lengthToReturn = randomizer.nextInt(this.maxLength + 1);
            iterator.next();

        } else if (quantifierChar == '+') {
            lengthToReturn = 1 + randomizer.nextInt(this.maxLength + 1);
            iterator.next();

        } else {
            lengthToReturn = 1;

        }
        return lengthToReturn;
    }


}