package football.StatsManagement.exception;

public class ResourceConflictException extends Exception {

  public ResourceConflictException() {
  }

  public ResourceConflictException(String message) {
    super(message);
  }

  public ResourceConflictException(String message, Throwable cause) {
    super(message, cause);
  }

  public ResourceConflictException(Throwable cause) {
    super(cause);
  }

  public ResourceConflictException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
