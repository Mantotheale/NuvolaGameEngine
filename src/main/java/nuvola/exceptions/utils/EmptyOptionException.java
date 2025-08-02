package nuvola.exceptions.utils;

public class EmptyOptionException extends RuntimeException {
    public EmptyOptionException() {
        super("The unwrapped option was empty");
    }
}
