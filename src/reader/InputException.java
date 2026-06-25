package reader;

public class InputException extends RuntimeException {
    public InputException(String message) {
        super(message);
    }
    public InputException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
