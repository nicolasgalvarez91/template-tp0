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
            if (actualCharInExpression == '.') {
                wordToReturn = wordToReturn + this.processDotToken(stringIterator);
            } else if (actualCharInExpression == '[') {
                wordToReturn = wordToReturn + this.processSetToken(stringIterator);

            } else if (actualCharInExpression == '\\') {
                wordToReturn = wordToReturn + this.processBackslashToken(stringIterator);
            } else {
                wordToReturn = wordToReturn + this.processLiteralToken(stringIterator);
            }
            actualCharInExpression = stringIterator.current();
        }
        return wordToReturn;
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
        char currentChar = iterator.next();
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
        char currentChar = iterator.next();

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

    private boolean isAQuantifierChar(char regularExpressionChar) {
        if (regularExpressionChar == '*' || regularExpressionChar == '?' || regularExpressionChar == '+') {
            return true;
        } else {
            return false;
        }

    }

    private String stringForDotToken(StringCharacterIterator iterator) {
        String stringToReturn = "";
        Random randomizer = new Random();
        char currentChar = iterator.current();
        int stringLongitude = 1;

        stringLongitude = this.getLengthForQuantifier(iterator);


        for (int i = 0; i < stringLongitude ; i++) {
            int randomNumber = 33 + randomizer.nextInt(127 - 33); // getting a random number between 33 and 126
            stringToReturn = stringToReturn + (char) randomNumber;
        }
        return stringToReturn;
    }

    private String stringForLiteral(char literal , StringCharacterIterator iterator) {
        String stringToReturn = "";
        char currentChar = iterator.current();
        int stringLength = 1;
        stringLength = this.getLengthForQuantifier(iterator);

        Random randomizer = new Random();
        for (int i = 0 ; i < stringLength; i++) {
            stringToReturn = stringToReturn + literal;
        }
        return stringToReturn;
    }

    private String stringWithSetToken(ArrayList<Character> setOfChars, StringCharacterIterator iterator) {
        String stringToReturn = "";
        int lengthToReturn = this.getLengthForQuantifier(iterator);
        int numberOfCharsInSet = setOfChars.size();
        Random randomizer = new Random();
        if (numberOfCharsInSet > 0) {
            for (int i = 0; i < lengthToReturn; i++) {
                int randomSetPosition = randomizer.nextInt(numberOfCharsInSet);
                stringToReturn = stringToReturn + setOfChars.get(randomSetPosition);
            }
        }

        return stringToReturn;
    }

    private int getLengthForQuantifier(StringCharacterIterator iterator) {
        int lengthToReturn;
        char quantifierChar = iterator.current();
        Random randomizer = new Random();

            switch (quantifierChar) {
                case '?':
                    lengthToReturn = randomizer.nextInt(2);
                    iterator.next();
                    break;
                case '*':
                    lengthToReturn = randomizer.nextInt(this.maxLength + 1);
                    iterator.next();
                    break;
                case '+':
                    lengthToReturn = 1 + randomizer.nextInt(this.maxLength + 1);
                    iterator.next();
                    break;
                default:
                    lengthToReturn = 1;
                    break;
            }



        return lengthToReturn;
    }


}