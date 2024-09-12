package football.StatsManagement.exception;

public class FootballException extends Exception {

  public FootballException() {
  }

  public FootballException(String message) {
    super(message);
  }

  public FootballException(String message, Throwable cause) {
    super(message, cause);
  }

  public FootballException(Throwable cause) {
    super(cause);
  }

  public FootballException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
