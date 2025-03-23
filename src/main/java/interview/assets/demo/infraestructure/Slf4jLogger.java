package interview.assets.demo.infraestructure;

import interview.assets.demo.domain.interfaces.ILogger;
import org.slf4j.LoggerFactory;

public class Slf4jLogger implements ILogger {

  private final org.slf4j.Logger logger;

  public Slf4jLogger(Class<?> clazz) {
    this.logger = LoggerFactory.getLogger(clazz);
  }

  @Override
  public void debug(String message) {
    logger.debug(message);
  }

  @Override
  public void info(String message) {
    logger.info(message);
  }

  @Override
  public void warn(String message) {
    logger.warn(message);
  }

  @Override
  public void warn(String message, Throwable throwable) {
    logger.warn(message, throwable);
  }

  @Override
  public void error(String message) {
    logger.error(message);
  }

  @Override
  public void error(String message, Throwable throwable) {
    logger.error(message, throwable);
  }
}