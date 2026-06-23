package db;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public DatabaseException(Throwable throwable) {
        super(throwable);
    }
}
