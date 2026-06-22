package migration;

public class MigrationException extends RuntimeException {
  public MigrationException(String message) {
    super(message);
  }

  public MigrationException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
