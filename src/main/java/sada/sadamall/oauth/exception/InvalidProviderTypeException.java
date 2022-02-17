package sada.sadamall.oauth.exception;

public class InvalidProviderTypeException extends IllegalArgumentException{
    public InvalidProviderTypeException() {
        super("Provider type is invalid.");
    }

    public InvalidProviderTypeException(String s) {
        super(s);
    }
}
