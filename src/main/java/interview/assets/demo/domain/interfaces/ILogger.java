package interview.assets.demo.domain.interfaces;

public interface ILogger {

  void info(String message);

  void error(String message);

  void error(String message, Throwable throwable);
}
