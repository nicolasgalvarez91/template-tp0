package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.text.StringCharacterIterator;
import java.util.List;
import java.util.Random;

public class RegExGenerator {

    private int maxLength;

    public RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
    }


    public List<String> generate(String regEx, int numberOfResults) {
        ArrayList<String> arrayToReturn = new ArrayList<String>();
        for (int i=0 ; i < numberOfResults ; i++){
            arrayToReturn.add(this.getWordForRegularExpresion(regEx));
        }
        System.out.println(arrayToReturn);
        return arrayToReturn;
    }

    private String getWordForRegularExpresion(String regularExpression){
        String wordToReturn="";
        StringCharacterIterator stringIterator = new StringCharacterIterator(regularExpression);
        char actualCharInExpression = stringIterator.first();
        while (actualCharInExpression != stringIterator.DONE){
            switch (actualCharInExpression){
                case '.':
                    actualCharInExpression = stringIterator.next();
                    if (actualCharInExpression != stringIterator.DONE){
                        if (this.isAQuantifierChar(actualCharInExpression)){
                            wordToReturn = wordToReturn + this.stringForDotToken(actualCharInExpression);
                            actualCharInExpression = stringIterator.next();
                        } else{
                            wordToReturn = wordToReturn + this.stringForDotToken('\0');
                        }


                    } else{
                        wordToReturn = wordToReturn + this.stringForDotToken('\0');
                    }
                    break;
                case '[':
                    actualCharInExpression = stringIterator.next();
                    ArrayList<Character> setOfPossibleChars = new ArrayList<Character>();
                    while(actualCharInExpression !=']'){
                        if (actualCharInExpression == '\\') {
                            actualCharInExpression = stringIterator.next();
                            setOfPossibleChars.add(actualCharInExpression);
                        } else if (actualCharInExpression != stringIterator.DONE){
                            setOfPossibleChars.add(actualCharInExpression);
                        } else
                            return "";
                        actualCharInExpression = stringIterator.next();
                    }
                    actualCharInExpression = stringIterator.next();
                    if (this.isAQuantifierChar(actualCharInExpression)){
                        wordToReturn = wordToReturn + this.stringWithSetToken(setOfPossibleChars, actualCharInExpression);
                        actualCharInExpression = stringIterator.next();
                    } else {
                        wordToReturn = wordToReturn + this.stringWithSetToken(setOfPossibleChars, '\0');
                    }
                    break;
                case '\\':
                    actualCharInExpression = stringIterator.next();
                    char escapedChar = actualCharInExpression;
                    actualCharInExpression = stringIterator.next();
                    if (this.isAQuantifierChar(actualCharInExpression)) {
                        wordToReturn = wordToReturn + this.stringForLiteral(escapedChar, actualCharInExpression);
                        actualCharInExpression = stringIterator.next();
                    }else
                        wordToReturn = wordToReturn + this.stringForLiteral(escapedChar,'\0');
                    break;
                default:
                    char literal = actualCharInExpression;
                    actualCharInExpression = stringIterator.next();
                    if (this.isAQuantifierChar(actualCharInExpression)) {
                        wordToReturn = wordToReturn + this.stringForLiteral(literal, actualCharInExpression);
                        actualCharInExpression = stringIterator.next();
                    }else
                        wordToReturn = wordToReturn + this.stringForLiteral(literal,'\0');
                    break;



            }
        }
        return wordToReturn;
    }



    private boolean isAQuantifierChar(char regularExpressionChar){
      if (regularExpressionChar == '*' || regularExpressionChar == '?' || regularExpressionChar == '+')
          return true;
      else
          return false;

    }


    private String stringForLiteral(char literal , char quantifierChar){
        String stringToReturn = "";
        int stringLength = this.getLengthForQuantifier(quantifierChar);
        Random randomizer = new Random();
        for (int i = 0 ; i < stringLength; i++){
            stringToReturn = stringToReturn + literal;
        }
        return stringToReturn;
    }

    private String stringWithSetToken(ArrayList<Character> setOfChars, char quantifierChar){
        String stringToReturn = "";
        int lengthToReturn = this.getLengthForQuantifier(quantifierChar);
        int numberOfCharsInSet = setOfChars.size();
        Random randomizer = new Random();

        for (int i = 0; i < lengthToReturn; i++){
            int randomSetPosition = randomizer.nextInt(numberOfCharsInSet - 1);
            stringToReturn = stringToReturn + setOfChars.get(randomSetPosition);
        }

        return stringToReturn;
    }

    private int getLengthForQuantifier(char quantifierChar){
        int lengthToReturn = 0;
        Random randomizer = new Random();
        if (quantifierChar == '\0'){
            lengthToReturn = 1;
        } else {
            switch (quantifierChar){
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

    private String stringForDotToken(char quantifierChar){
        String stringToReturn = "";
        Random randomizer = new Random();
        int stringLongitude;
        stringLongitude = this.getLengthForQuantifier(quantifierChar);

        for (int i = 0; i<stringLongitude ; i++) {
            int randomNumber = 33 + randomizer.nextInt(126 - 33); // getting a random number between 33 and 126
            stringToReturn = stringToReturn + (char) randomNumber;
        }
        return stringToReturn;
    }
}