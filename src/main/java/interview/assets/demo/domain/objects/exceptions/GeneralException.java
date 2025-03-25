package interview.assets.demo.domain.objects.exceptions;

public class GeneralException extends Exception {

  public GeneralException(String message, Throwable cause) {
    super(message, cause);
  }

  public GeneralException(String message) {
    super(message);
  }
}