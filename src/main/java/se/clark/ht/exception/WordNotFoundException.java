package se.clark.ht.exception;

public class WordNotFoundException extends Exception{

    public WordNotFoundException() {
    }

    public WordNotFoundException(String wordName) {
        super("word '" + wordName + "' doesn't even exist.");
    }

}
